package com.example.bit240164_foodmanager.data.repository

import android.content.Context
import android.util.Log
import com.example.bit240164_foodmanager.data.model.Dish
import com.example.bit240164_foodmanager.data.model.DishCategory
import com.example.bit240164_foodmanager.data.remote.RetrofitClient
import com.example.bit240164_foodmanager.data.utils.SeedData
import com.example.bit240164_foodmanager.utils.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.util.UUID

class FoodRepository(context: Context) {
    private val api = RetrofitClient.api
    private val networkMonitor = NetworkMonitor(context.applicationContext)
    private val scope = CoroutineScope(Dispatchers.IO)

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = networkMonitor.isCurrentlyOnline()
    )

    suspend fun getAllCategories(): List<DishCategory> = withContext(Dispatchers.IO) {
        try {
            val categories = api.getAllCategories()
            if (categories.isNotEmpty()) return@withContext categories
        } catch (e: Exception) {
            Log.e("REPO", "Error fetching categories", e)
        }
        return@withContext SeedData.categories
    }

    suspend fun getCategoryById(id: String): DishCategory = withContext(Dispatchers.IO) {
        try {
            val list = api.getCategoryById(id)
            if (list.isNotEmpty()) return@withContext list.first()
        } catch (_: Exception) {}
        return@withContext getAllCategories().find { id == it.id } ?: SeedData.categories.first()
    }

    suspend fun getAllDishes(): List<Dish> = withContext(Dispatchers.IO) {
        try {
            val dishes = api.getAllDishes()
            if (dishes.isNotEmpty()) return@withContext dishes
        } catch (e: Exception) {
            Log.e("REPO", "Error fetching dishes", e)
        }
        return@withContext SeedData.dishes
    }

    suspend fun getDishById(id: String): Dish = withContext(Dispatchers.IO) {
        try {
            val list = api.getDishById(id)
            if (list.isNotEmpty()) return@withContext list.first()
        } catch (_: Exception) {}
        return@withContext getAllDishes().find { id == it.id } ?: SeedData.dishes.first()
    }

    suspend fun createDish(dish: Dish): Dish = withContext(Dispatchers.IO) {
        val assignedId = if (dish.id.isBlank()) UUID.randomUUID().toString() else dish.id
        val newDish = dish.copy(id = assignedId)
        return@withContext try {
            api.createDish(newDish)
        } catch (_: Exception) {
            newDish
        }
    }

    suspend fun updateDish(id: String, dish: Dish): Dish = withContext(Dispatchers.IO) {
        val updatedDish = dish.copy(id = id)
        return@withContext try {
            api.updateDish(id, updatedDish)
        } catch (_: Exception) {
            updatedDish
        }
    }

    suspend fun deleteDish(id: String) = withContext(Dispatchers.IO) {
        try {
            api.deleteDish(id)
        } catch (_: Exception) {}
    }
}
