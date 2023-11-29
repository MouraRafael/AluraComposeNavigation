package br.com.alura.panucci.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.alura.panucci.model.Product
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.HighlightsListScreen


internal const val highlightListRoute = "highlights"
fun NavGraphBuilder.highlightsListScreen(navController: NavHostController) {
    composable(AppDestinations.Highlights.route) {
        HighlightsListScreen(
            products = sampleProducts,
            onNavigateToDetails = { p: Product ->
                val promoCode = "ALURA"
                navController.navigateToDetails("${p.id}?promoCode=${promoCode}")
            },
            onNavigateToCheckout = {
                navController.navigateToCheckout()
            },

            )
    }
}

fun NavController.navigateToHighLights(){
    navigate(highlightListRoute)
}