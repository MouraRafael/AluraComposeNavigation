package br.com.alura.panucci.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.alura.panucci.ui.components.BottomAppBarItem

@Composable
fun PanucchiNavHost(navController:NavHostController) {
    NavHost(
        navController = navController,
        startDestination = highlightListRoute
    ) {
        highlightsListScreen(navController)
        menuScreen(navController)
        drinksScreen(navController)

        productDetailsScreen(navController)
        productCheckoutScreen(navController)
    }

}








val bottomAppBarItems = listOf(
   BottomAppBarItem.HighLights,
    BottomAppBarItem.Menu,
    BottomAppBarItem.Drinks
)



