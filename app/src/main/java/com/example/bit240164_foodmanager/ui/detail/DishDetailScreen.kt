package com.example.bit240164_foodmanager.ui.detail

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bit240164_foodmanager.R
import com.example.bit240164_foodmanager.data.model.Dish
import com.example.bit240164_foodmanager.data.model.DishCategory
import com.example.bit240164_foodmanager.data.repository.FoodRepository
import com.example.bit240164_foodmanager.utils.AppLanguage
import com.example.bit240164_foodmanager.utils.LocalAppLanguageState
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishDetailScreen(
    dishId: String,
    onBack: () -> Unit,
    onEdit: (String) -> Unit
) {
    val context = LocalContext.current
    val languageState = LocalAppLanguageState.current
    val repository = remember { FoodRepository(context) }

    var dish by remember { mutableStateOf<Dish?>(null) }
    var category by remember { mutableStateOf<DishCategory?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(dishId) {
        try {
            isLoading = true
            val loadedDish = repository.getDishById(dishId)
            dish = loadedDish
            if (loadedDish.BIT240164_DishCategoryId.isNotBlank()) {
                category = try {
                    repository.getCategoryById(loadedDish.BIT240164_DishCategoryId)
                } catch (_: Exception) { null }
            }
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoading = false
        }
    }

    val formattedPrice = remember(dish?.BIT240164_Price) {
        val price = dish?.BIT240164_Price ?: 0.0
        val format = NumberFormat.getNumberInstance(Locale.GERMANY)
        "${format.format(price)} VND"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(dish?.BIT240164_Name ?: stringResource(R.string.dish_detail)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val nextLang = if (languageState.currentLanguage == AppLanguage.ENGLISH)
                                AppLanguage.VIETNAMESE else AppLanguage.ENGLISH
                            languageState.switchLanguage(context, nextLang)
                        }
                    ) {
                        Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = if (languageState.currentLanguage == AppLanguage.ENGLISH) "EN" else "VI",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = { onEdit(dishId) }) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit))
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text("Error: $error", color = MaterialTheme.colorScheme.error)
                }
            }
            dish != null -> {
                val d = dish!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (d.BIT240164_DishImage.isNotEmpty()) {
                        Column {
                            Text(
                                text = stringResource(R.string.images),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(d.BIT240164_DishImage) { img ->
                                    Card(
                                        elevation = CardDefaults.cardElevation(4.dp),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Box {
                                            AsyncImage(
                                                model = img.ImageUrl,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(180.dp)
                                                    .clip(RoundedCornerShape(12.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                            if (img.IsThumbnail) {
                                                Surface(
                                                    modifier = Modifier
                                                        .align(Alignment.TopStart)
                                                        .padding(8.dp),
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = stringResource(R.string.thumbnail),
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onPrimary
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = d.BIT240164_Name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            if (category != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "${stringResource(R.string.category)}: ", fontWeight = FontWeight.Bold)
                                    Text(category!!.BIT240164_Name)
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "${stringResource(R.string.price)}: ", fontWeight = FontWeight.Bold)
                                Text(
                                    text = formattedPrice,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = if (d.BIT240164_PreparationTime <= 15) Color(0xFFE3F2FD) else Color(0xFFF5F5F5),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${stringResource(R.string.preparation_time)}: ${d.BIT240164_PreparationTime} min (${if (d.BIT240164_PreparationTime <= 15) stringResource(R.string.quick) else stringResource(R.string.regular)})",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (d.BIT240164_PreparationTime <= 15) Color(0xFF1976D2) else Color(0xFF616161)
                                    )
                                }
                            }

                            Row {
                                Surface(
                                    color = if (d.BIT240164_IsAvailable) Color(0xE8E8F5E9) else Color(0xFFFFEBEE),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (d.BIT240164_IsAvailable) stringResource(R.string.available) else stringResource(R.string.unavailable),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (d.BIT240164_IsAvailable) Color(0xFF2E7D32) else Color(0xFFC62828)
                                    )
                                }
                            }

                            HorizontalDivider()

                            Column {
                                Text(text = stringResource(R.string.description), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text(text = d.BIT240164_Description.ifBlank { "N/A" }, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
