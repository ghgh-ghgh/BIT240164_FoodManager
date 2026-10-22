package com.example.bit240164_foodmanager.data.remote

import com.example.bit240164_foodmanager.data.model.Dish
import com.example.bit240164_foodmanager.data.model.DishCategory
import retrofit2.http.*

interface ApiService {
    @GET("BIT240164_DishCategory")
    suspend fun getAllCategories(): List<DishCategory>

    @GET("BIT240164_DishCategory/{id}")
    suspend fun getCategoryById(@Path("id") id: String): List<DishCategory>

    @POST("BIT240164_DishCategory")
    suspend fun createCategory(@Body category: DishCategory): DishCategory

    @PUT("BIT240164_DishCategory/{id}")
    suspend fun updateCategory(@Path("id") id: String, @Body category: DishCategory): DishCategory

    @DELETE("BIT240164_DishCategory/{id}")
    suspend fun deleteCategory(@Path("id") id: String)

    @GET("BIT240164_Dish")
    suspend fun getAllDishes(): List<Dish>

    @GET("BIT240164_Dish/{id}")
    suspend fun getDishById(@Path("id") id: String): List<Dish>

    @POST("BIT240164_Dish")
    suspend fun createDish(@Body dish: Dish): Dish

    @PUT("BIT240164_Dish/{id}")
    suspend fun updateDish(@Path("id") id: String, @Body dish: Dish): Dish

    @DELETE("BIT240164_Dish/{id}")
    suspend fun deleteDish(@Path("id") id: String)
}
