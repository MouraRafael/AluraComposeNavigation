package br.com.alura.panucci.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import androidx.navigation.navigation
import br.com.alura.panucci.ui.components.BottomAppBarItem


internal const val homeGraphRoute = "home"
fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    navigation(startDestination = highlightListRoute, route = homeGraphRoute) {
        highlightsListScreen(navController)
        menuScreen(navController)
        drinksScreen(navController)
    }
}

fun NavController.navigateToHomeGraph(){
    navigate(homeGraphRoute)
}

fun NavController.navigateSingleTopWithPopUpTo(
    item: BottomAppBarItem
) {
    /*Esse When retorna um par com o valor fixo da rota e a referencia da funcao como segundo argumento
                        * assum a val route vai receber a rota definida no par, e a val navigate vai receber a referencia da funcao do segundo argumento
                        * por exemplo, no caso para  Drinks a route vai receber a rota para a pagina de drinks
                        * e a navigate vai receber a referencia da funcao navigateToDrinks
                        * então poderei chamar a navigate to drinks usando a navigate
                        * */
    val (route, navigate) = when (item) {
        BottomAppBarItem.Drinks -> Pair(drinksRoute, this::navigateToDrinks)
        BottomAppBarItem.HighLights -> Pair(
            highlightListRoute,
            this::navigateToHighLights
        )

        BottomAppBarItem.Menu -> Pair(menuRoute, this::navigateToMenu)
    }

    val navOptions = navOptions {
        launchSingleTop = true
        popUpTo(route)
    }
    navigate(navOptions)
}


