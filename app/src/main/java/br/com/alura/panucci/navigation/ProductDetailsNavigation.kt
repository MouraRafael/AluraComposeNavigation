package br.com.alura.panucci.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.alura.panucci.ui.screens.ProductDetailsScreen
import br.com.alura.panucci.ui.viewmodels.ProductDetailsViewModel


private const val productDetailsRoute = "productDetails"
fun NavGraphBuilder.productDetailsScreen(navController: NavHostController) {
    composable(
        "$productDetailsRoute/{productId}?promoCode={promoCode}",
        arguments = listOf(navArgument("promoCode") { nullable = true })
    ) { backStackEntry ->
        backStackEntry.arguments?.getString("productId")?.let {id->
            val promoCode = backStackEntry.arguments?.getString("promoCode") != null

            val viewmodel = viewModel<ProductDetailsViewModel>()
            val uiState by viewmodel.uiState.collectAsState()

            LaunchedEffect(Unit){
                viewmodel.findProductById(id,promoCode)
            }

            ProductDetailsScreen(
                uiState = uiState,
                onNavigateToCheckout = {
                    navController.navigateToCheckout()
                },
                onTryFindProductAgain = {
                    viewmodel.findProductById(id)
                },
                onBackStack = {
                    navController.navigateUp()
                }
            )



        }?: LaunchedEffect(Unit) { navController.popBackStack() }











    }
}

fun NavController.navigateToDetails(id:String){
    navigate("$productDetailsRoute/$id")
}