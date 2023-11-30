package br.com.alura.panucci

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions

import br.com.alura.panucci.navigation.PanucchiNavHost

import br.com.alura.panucci.navigation.drinksRoute
import br.com.alura.panucci.navigation.highlightListRoute
import br.com.alura.panucci.navigation.menuRoute
import br.com.alura.panucci.navigation.navigateToCheckout
import br.com.alura.panucci.navigation.navigateToDrinks
import br.com.alura.panucci.navigation.navigateToHighLights
import br.com.alura.panucci.navigation.navigateToMenu
import br.com.alura.panucci.ui.components.BottomAppBarItem
import br.com.alura.panucci.ui.components.PanucciBottomAppBar
import br.com.alura.panucci.ui.components.bottomAppBarItems
import br.com.alura.panucci.ui.screens.*
import br.com.alura.panucci.ui.theme.PanucciTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val currentBackStackEntryState by navController.currentBackStackEntryAsState()
            val currentDestination = currentBackStackEntryState?.destination

            navController.addOnDestinationChangedListener { _, _, _ ->
                val routes = navController.backQueue.map { it.destination.route }
                println(routes)
            }


            PanucciTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val currentRoute = currentDestination?.route
                    val selectedItem by remember(currentDestination) {
                        val item = when (currentRoute) {
                            highlightListRoute -> BottomAppBarItem.HighLights
                            menuRoute -> BottomAppBarItem.Menu
                            drinksRoute -> BottomAppBarItem.Drinks

                            else->BottomAppBarItem.HighLights
                        }

                        mutableStateOf(item)
                    }

                    val containsInBottomAppBarItems = when(currentRoute){
                        highlightListRoute,menuRoute, drinksRoute -> true
                        else->false
                    }

                    val isShowFab = when (currentRoute) {
                        menuRoute, drinksRoute -> true
                        else -> false
                    }

                    PanucciApp(
                        bottomAppBarItemSelected = selectedItem,
                        onBottomAppBarItemSelectedChange = {item->
                            /*Esse When retorna um par com o valor fixo da rota e a referencia da funcao como segundo argumento
                            * assum a val route vai receber a rota definida no par, e a val navigate vai receber a referencia da funcao do segundo argumento
                            * por exemplo, no caso para  Drinks a route vai receber a rota para a pagina de drinks
                            * e a navigate vai receber a referencia da funcao navigateToDrinks
                            * então poderei chamar a navigate to drinks usando a navigate
                            * */
                            val (route,navigate)= when(item){
                                BottomAppBarItem.Drinks -> Pair(drinksRoute,navController::navigateToDrinks)
                                BottomAppBarItem.HighLights -> Pair(highlightListRoute,navController::navigateToHighLights)
                                BottomAppBarItem.Menu -> Pair(menuRoute,navController::navigateToMenu)
                            }

                            val navOptions = navOptions {
                                launchSingleTop=true
                                popUpTo(route)
                            }
                            navigate(navOptions)
                        },
                        onFabClick = {
                            navController.navigateToCheckout()
                        },
                        isShowTopBar = containsInBottomAppBarItems,
                        isShowBottomAppBar = containsInBottomAppBarItems,
                        isShowFab = isShowFab
                    ) {
                        PanucchiNavHost(navController = navController)
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanucciApp(
    bottomAppBarItemSelected: BottomAppBarItem = bottomAppBarItems.first(),
    onBottomAppBarItemSelectedChange: (BottomAppBarItem) -> Unit = {},
    onFabClick: () -> Unit = {},
    isShowTopBar: Boolean = false,
    isShowBottomAppBar: Boolean = false,
    isShowFab: Boolean = false,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            if (isShowTopBar) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Ristorante Panucci")
                    },
                )
            }
        },
        bottomBar = {
            if (isShowBottomAppBar) {

                PanucciBottomAppBar(
                    item = bottomAppBarItemSelected,
                    items = bottomAppBarItems,
                    onItemChange = onBottomAppBarItemSelectedChange,
                )
            }
        },
        floatingActionButton = {
            if (isShowFab) {

                FloatingActionButton(
                    onClick = onFabClick
                ) {
                    Icon(
                        Icons.Filled.PointOfSale,
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun PanucciAppPreview() {
    PanucciTheme {
        Surface {
            PanucciApp {}
        }
    }
}