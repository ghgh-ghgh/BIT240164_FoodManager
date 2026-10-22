package com.example.bit240164_foodmanager.ui.form

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bit240164_foodmanager.R
import com.example.bit240164_foodmanager.data.model.Dish
import com.example.bit240164_foodmanager.data.model.DishCategory
import com.example.bit240164_foodmanager.data.model.DishImage
import com.example.bit240164_foodmanager.data.repository.FoodRepository
import com.example.bit240164_foodmanager.utils.AppLanguage
import com.example.bit240164_foodmanager.utils.LocalAppLanguageState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishFormScreen(
    dishId: String? = null,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val context = LocalContext.current
    val languageState = LocalAppLanguageState.current
    val repository = remember { FoodRepository(context) }
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var prepTime by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isAvailable by remember { mutableStateOf(true) }
    var selectedCategoryId by remember { mutableStateOf("") }
    var newImageUrl by remember { mutableStateOf("") }
    var imageList by remember { mutableStateOf<List<DishImage>>(emptyList()) }

    var categories by remember { mutableStateOf<List<DishCategory>>(emptyList()) }
    var allExistingDishes by remember { mutableStateOf<List<Dish>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            isLoading = true
            categories = repository.getAllCategories()
            allExistingDishes = repository.getAllDishes()

            if (dishId != null) {
                val dish = repository.getDishById(dishId)
                name = dish.BIT240164_Name
                price = if (dish.BIT240164_Price % 1 == 0.0) dish.BIT240164_Price.toLong().toString() else dish.BIT240164_Price.toString()
                prepTime = dish.BIT240164_PreparationTime.toString()
                description = dish.BIT240164_Description
                isAvailable = dish.BIT240164_IsAvailable
                selectedCategoryId = dish.BIT240164_DishCategoryId
                imageList = dish.BIT240164_DishImage
            } else if (categories.isNotEmpty()) {
                selectedCategoryId = categories.first().id
            }
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoading = false
        }
    }

    val errNameEmpty = stringResource(R.string.error_name_empty)
    val errPriceInvalid = stringResource(R.string.error_price_invalid)
    val errPrepInvalid = stringResource(R.string.error_prep_time_invalid)
    val errCategoryRequired = stringResource(R.string.error_category_required)
    val errDuplicateName = stringResource(R.string.error_duplicate_name)

    fun validate(): String? {
        if (name.isBlank()) return errNameEmpty
        val p = price.toDoubleOrNull()
        if (p == null || p <= 0) return errPriceInvalid
        val t = prepTime.toIntOrNull()
        if (t == null || t <= 0) return errPrepInvalid
        if (selectedCategoryId.isBlank()) return errCategoryRequired

        val trimmedName = name.trim()
        val duplicate = allExistingDishes.any { d ->
            d.id != dishId &&
                    d.BIT240164_DishCategoryId == selectedCategoryId &&
                    d.BIT240164_Name.trim().equals(trimmedName, ignoreCase = true)
        }
        if (duplicate) return errDuplicateName

        return null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (dishId == null) stringResource(R.string.add_dish) else stringResource(R.string.edit_dish)) },
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
                }
            )
        }
    ) { padding ->
        if (isLoading && categories.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; error = null },
                    label = { Text("${stringResource(R.string.name)} *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it; error = null },
                        label = { Text("${stringResource(R.string.price)} (VND) *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = prepTime,
                        onValueChange = { prepTime = it; error = null },
                        label = { Text("${stringResource(R.string.preparation_time)} (min) *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "${stringResource(R.string.category)} *",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        categories.forEach { cat ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = selectedCategoryId == cat.id,
                                    onClick = { selectedCategoryId = cat.id; error = null }
                                )
                                Text(text = cat.BIT240164_Name, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(checked = isAvailable, onCheckedChange = { isAvailable = it })
                    Text(text = stringResource(R.string.available), fontWeight = FontWeight.Medium)
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.images),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = newImageUrl,
                                onValueChange = { newImageUrl = it },
                                label = { Text(stringResource(R.string.add_image_url)) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            IconButton(
                                onClick = {
                                    val url = newImageUrl.trim()
                                    if (url.isNotBlank()) {
                                        val isFirst = imageList.isEmpty()
                                        imageList = imageList + DishImage(ImageUrl = url, IsThumbnail = isFirst)
                                        newImageUrl = ""
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Image")
                            }
                        }

                        imageList.forEachIndexed { index, img ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                AsyncImage(
                                    model = img.ImageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp).clip(RoundedCornerShape(6.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = img.ImageUrl, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = img.IsThumbnail,
                                            onClick = {
                                                imageList = imageList.mapIndexed { i, item ->
                                                    item.copy(IsThumbnail = (i == index))
                                                }
                                            }
                                        )
                                        Text(text = stringResource(R.string.thumbnail), style = MaterialTheme.typography.labelMedium)
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        val newList = imageList.filterIndexed { i, _ -> i != index }
                                        if (img.IsThumbnail && newList.isNotEmpty()) {
                                            imageList = newList.mapIndexed { i, item -> item.copy(IsThumbnail = (i == 0)) }
                                        } else {
                                            imageList = newList
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }

                if (error != null) {
                    Text(text = error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        val validationError = validate()
                        if (validationError != null) {
                            error = validationError
                            return@Button
                        }

                        scope.launch {
                            isLoading = true
                            error = null
                            try {
                                var finalImages = imageList
                                if (finalImages.isNotEmpty() && !finalImages.any { it.IsThumbnail }) {
                                    finalImages = finalImages.mapIndexed { i, img -> img.copy(IsThumbnail = (i == 0)) }
                                }

                                val dishToSave = Dish(
                                    id = dishId ?: "",
                                    BIT240164_Name = name.trim(),
                                    BIT240164_Price = price.toDouble(),
                                    BIT240164_PreparationTime = prepTime.toInt(),
                                    BIT240164_IsAvailable = isAvailable,
                                    BIT240164_Description = description.trim(),
                                    BIT240164_DishCategoryId = selectedCategoryId,
                                    BIT240164_DishImage = finalImages
                                )

                                if (dishId == null) {
                                    repository.createDish(dishToSave)
                                } else {
                                    repository.updateDish(dishId, dishToSave)
                                }
                                onSaved()
                            } catch (e: Exception) {
                                error = e.message
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text(stringResource(R.string.save), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
