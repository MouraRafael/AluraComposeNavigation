package br.com.alura.panucci.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.alura.panucci.model.Product
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.MenuListScreen

internal const val menuRoute ="menu"
fun NavGraphBuilder.menuScreen(navController: NavHostController) {
    composable(menuRoute) {
        MenuListScreen(
            products = sampleProducts,
            onNavigateToDetails = { p: Product ->
                navController.navigateToDetails(p.id)
            }
        )
    }
}

fun NavController.navigateToMenu(){
    navigate(menuRoute)
}