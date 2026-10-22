package com.example.bit240164_foodmanager.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bit240164_foodmanager.data.model.Dish
import com.example.bit240164_foodmanager.data.model.DishCategory
import com.example.bit240164_foodmanager.data.repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DishListViewModel(application: Application) : AndroidViewModel(application) {
    val repository = FoodRepository(application)

    private val _allDishes = MutableStateFlow<List<Dish>>(emptyList())
    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()

    private val _categories = MutableStateFlow<List<DishCategory>>(emptyList())
    val categories: StateFlow<List<DishCategory>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val isOnline: StateFlow<Boolean> = repository.isOnline

    var searchQuery = ""
    var selectedCategoryId: String? = null
    var onlyAvailable = false
    var minPrice: Double? = null
    var maxPrice: Double? = null
    var sortBy = "none"

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _categories.value = repository.getAllCategories()
                _allDishes.value = repository.getAllDishes()
                applyFilters()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error loading data"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun applyFilters() {
        var result = _allDishes.value

        if (searchQuery.isNotBlank()) {
            result = result.filter { it.BIT240164_Name.contains(searchQuery, ignoreCase = true) }
        }

        selectedCategoryId?.let { catId ->
            result = result.filter { it.BIT240164_DishCategoryId == catId }
        }

        if (onlyAvailable) {
            result = result.filter { it.BIT240164_IsAvailable }
        }

        minPrice?.let { min -> result = result.filter { it.BIT240164_Price >= min } }
        maxPrice?.let { max -> result = result.filter { it.BIT240164_Price <= max } }

        result = when (sortBy) {
            "price_asc" -> result.sortedBy { it.BIT240164_Price }
            "price_desc" -> result.sortedByDescending { it.BIT240164_Price }
            "time_asc" -> result.sortedBy { it.BIT240164_PreparationTime }
            else -> result
        }

        _dishes.value = result
    }

    fun resetFilters() {
        searchQuery = ""
        selectedCategoryId = null
        onlyAvailable = false
        minPrice = null
        maxPrice = null
        sortBy = "none"
        applyFilters()
    }

    fun deleteDish(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteDish(id)
                loadData()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
