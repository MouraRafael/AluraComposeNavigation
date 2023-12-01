package br.com.alura.panucci.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.alura.panucci.model.Product
import br.com.alura.panucci.ui.screens.HighlightsListScreen
import br.com.alura.panucci.ui.viewmodels.HighlightsListViewModel


internal const val highlightListRoute = "highlights"
fun NavGraphBuilder.highlightsListScreen(navController: NavHostController) {
    composable(highlightListRoute) {
        val viewModel = viewModel<HighlightsListViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        HighlightsListScreen(
            uiState = uiState,
            onNavigateToDetails = { p: Product ->
                val promoCode = "ALURA"
                navController.navigateToDetails("${p.price.toPlainString()}?promoCode=${promoCode}")
            },
            onNavigateToCheckout = {
                navController.navigateToCheckout()
            },

            )
    }
}

fun NavController.navigateToHighLights(navOptions: NavOptions?=null){
    navigate(highlightListRoute,navOptions)
}