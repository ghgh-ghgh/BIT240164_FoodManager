package com.example.bit240164_foodmanager.data.model

data class Dish(
    val id: String = "",
    val BIT240164_Name: String = "",
    val BIT240164_Price: Double = 0.0,
    val BIT240164_PreparationTime: Int = 0,
    val BIT240164_IsAvailable: Boolean = true,
    val BIT240164_Description: String = "",
    val BIT240164_DishCategoryId: String = "",
    val BIT240164_DishImage: List<DishImage> = emptyList()
)
