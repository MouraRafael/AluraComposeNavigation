package br.com.alura.panucci.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.ProductDetailsScreen
import java.math.BigDecimal


private const val productDetailsRoute = "productDetails"
fun NavGraphBuilder.productDetailsScreen(navController: NavHostController) {
    composable(
        "${AppDestinations.ProductDetails.route}/{productId}?promoCode={promoCode}",
        arguments = listOf(navArgument("promoCode") { nullable = true })
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("productId")
        println(id)

        val promoCode = backStackEntry.arguments?.getString("promoCode")



        sampleProducts.find { id == it.id }?.let { product ->

            val discount = when (promoCode) {
                "ALURA" -> BigDecimal("0.1")
                else -> BigDecimal.ZERO
            }
            val currentPrice = product.price

            ProductDetailsScreen(
                product = product.copy(price = currentPrice - (currentPrice * discount)),
                onNavigateToCheckout = {
                    navController.navigateToCheckout()
                }
            )
        } ?: LaunchedEffect(Unit) { navController.popBackStack() }

    }
}

fun NavController.navigateToDetails(id:String){
    navigate("$productDetailsRoute/$id")
}