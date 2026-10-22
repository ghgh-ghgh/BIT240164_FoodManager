package com.example.bit240164_foodmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bit240164_foodmanager.ui.detail.DishDetailScreen
import com.example.bit240164_foodmanager.ui.form.DishFormScreen
import com.example.bit240164_foodmanager.ui.list.DishListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            DishListScreen(
                onDishClick = { id -> navController.navigate("detail/$id") },
                onAddClick = { navController.navigate("form") }
            )
        }

        composable(
            "detail/{dishId}",
            arguments = listOf(navArgument("dishId") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("dishId") ?: return@composable
            DishDetailScreen(
                dishId = id,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate("form/$it") }
            )
        }

        composable("form") {
            DishFormScreen(
                dishId = null,
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(
            "form/{dishId}",
            arguments = listOf(navArgument("dishId") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("dishId")
            DishFormScreen(
                dishId = id,
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }
    }
}
