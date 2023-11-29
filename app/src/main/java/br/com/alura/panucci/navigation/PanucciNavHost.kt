package br.com.alura.panucci.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun PanucchiNavHost(navController:NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.Highlights.route
    ) {
        highlightsListScreen(navController)
        menuScreen(navController)
        drinksScreen(navController)

        productDetailsScreen(navController)
        productCheckoutScreen(navController)
    }

}











