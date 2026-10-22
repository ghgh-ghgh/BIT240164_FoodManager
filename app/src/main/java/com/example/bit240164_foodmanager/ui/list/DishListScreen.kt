package com.example.bit240164_foodmanager.ui.list

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bit240164_foodmanager.R
import com.example.bit240164_foodmanager.data.model.Dish
import com.example.bit240164_foodmanager.utils.AppLanguage
import com.example.bit240164_foodmanager.utils.LocalAppLanguageState
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishListScreen(
    onDishClick: (String) -> Unit,
    onAddClick: () -> Unit,
    viewModel: DishListViewModel = viewModel()
) {
    val context = LocalContext.current
    val languageState = LocalAppLanguageState.current

    val dishes by viewModel.dishes.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchText by remember { mutableStateOf(viewModel.searchQuery) }
    var showDeleteDialog by remember { mutableStateOf<String?>(null) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isOnline) Color(0xFF4CAF50) else Color(0xFFFF9800))
                        )
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
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = stringResource(R.string.filter))
                    }
                    IconButton(onClick = { showSortDialog = true }) {
                        Icon(Icons.Default.Sort, contentDescription = stringResource(R.string.sort))
                    }
                    IconButton(onClick = onAddClick) {
                        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Surface(
                color = if (isOnline) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isOnline) stringResource(R.string.online_mode) else stringResource(R.string.offline_mode),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "MSSV: BIT240164",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    viewModel.searchQuery = it
                    viewModel.applyFilters()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text(stringResource(R.string.search_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
                    }
                }
                dishes.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.no_dishes_found),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = dishes, key = { dish -> if (dish.id.isNotBlank()) dish.id else dish.hashCode() }) { dish ->
                            val categoryName = categories.find { it.id == dish.BIT240164_DishCategoryId }?.BIT240164_Name ?: ""
                            DishItem(
                                dish = dish,
                                categoryName = categoryName,
                                onClick = { onDishClick(dish.id) },
                                onDelete = { showDeleteDialog = dish.id }
                            )
                        }
                    }
                }
            }
        }
    }

    showDeleteDialog?.let { id ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text(stringResource(R.string.confirm_delete_title)) },
            text = { Text(stringResource(R.string.confirm_delete)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteDish(id)
                        showDeleteDialog = null
                    }
                ) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    if (showFilterDialog) {
        var tempCategory by remember { mutableStateOf(viewModel.selectedCategoryId) }
        var tempAvailable by remember { mutableStateOf(viewModel.onlyAvailable) }
        var tempMin by remember { mutableStateOf(viewModel.minPrice?.let { if (it % 1 == 0.0) it.toLong().toString() else it.toString() } ?: "") }
        var tempMax by remember { mutableStateOf(viewModel.maxPrice?.let { if (it % 1 == 0.0) it.toLong().toString() else it.toString() } ?: "") }

        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text(stringResource(R.string.filter)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.category), fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = tempCategory == null, onClick = { tempCategory = null })
                        Text(stringResource(R.string.all))
                    }
                    categories.forEach { cat ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = tempCategory == cat.id, onClick = { tempCategory = cat.id })
                            Text(cat.BIT240164_Name)
                        }
                    }

                    HorizontalDivider()

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = tempAvailable, onCheckedChange = { tempAvailable = it })
                        Text(stringResource(R.string.only_available))
                    }

                    OutlinedTextField(
                        value = tempMin,
                        onValueChange = { tempMin = it },
                        label = { Text(stringResource(R.string.min_price)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = tempMax,
                        onValueChange = { tempMax = it },
                        label = { Text(stringResource(R.string.max_price)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.selectedCategoryId = tempCategory
                    viewModel.onlyAvailable = tempAvailable
                    viewModel.minPrice = tempMin.toDoubleOrNull()
                    viewModel.maxPrice = tempMax.toDoubleOrNull()
                    viewModel.applyFilters()
                    showFilterDialog = false
                }) {
                    Text(stringResource(R.string.apply))
                }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = {
                        viewModel.resetFilters()
                        showFilterDialog = false
                    }) {
                        Text(stringResource(R.string.reset))
                    }
                    TextButton(onClick = { showFilterDialog = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            }
        )
    }

    if (showSortDialog) {
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title = { Text(stringResource(R.string.sort_by)) },
            text = {
                Column {
                    listOf(
                        "none" to stringResource(R.string.sort_default),
                        "price_asc" to stringResource(R.string.sort_price_asc),
                        "price_desc" to stringResource(R.string.sort_price_desc),
                        "time_asc" to stringResource(R.string.sort_prep_time_asc)
                    ).forEach { (key, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.sortBy = key
                                    viewModel.applyFilters()
                                    showSortDialog = false
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = viewModel.sortBy == key,
                                onClick = {
                                    viewModel.sortBy = key
                                    viewModel.applyFilters()
                                    showSortDialog = false
                                }
                            )
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showSortDialog = false }) {
                    Text(stringResource(R.string.close))
                }
            }
        )
    }
}

@Composable
fun DishItem(
    dish: Dish,
    categoryName: String,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val thumbnail = dish.BIT240164_DishImage.firstOrNull { it.IsThumbnail }
        ?: dish.BIT240164_DishImage.firstOrNull()

    val formattedPrice = remember(dish.BIT240164_Price) {
        val format = NumberFormat.getNumberInstance(Locale.GERMANY)
        "${format.format(dish.BIT240164_Price)} VND"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = thumbnail?.ImageUrl,
                contentDescription = dish.BIT240164_Name,
                modifier = Modifier
                    .size(85.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dish.BIT240164_Name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (categoryName.isNotBlank()) {
                    Text(
                        text = categoryName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = if (dish.BIT240164_PreparationTime <= 15) Color(0xFFE3F2FD) else Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (dish.BIT240164_PreparationTime <= 15) stringResource(R.string.quick) else stringResource(R.string.regular),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (dish.BIT240164_PreparationTime <= 15) Color(0xFF1976D2) else Color(0xFF616161)
                        )
                    }

                    Surface(
                        color = if (dish.BIT240164_IsAvailable) Color(0xE8E8F5E9) else Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (dish.BIT240164_IsAvailable) stringResource(R.string.available) else stringResource(R.string.unavailable),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (dish.BIT240164_IsAvailable) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete), tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
