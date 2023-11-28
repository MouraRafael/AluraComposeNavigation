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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.alura.panucci.model.Product
import br.com.alura.panucci.navigation.AppDestinations
import br.com.alura.panucci.navigation.bottomAppBarItems
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.components.BottomAppBarItem
import br.com.alura.panucci.ui.components.PanucciBottomAppBar
import br.com.alura.panucci.ui.screens.*
import br.com.alura.panucci.ui.theme.PanucciTheme
import java.math.BigDecimal

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
                    val selectedItem by remember(currentDestination) {
                        val item = currentDestination?.let { destination ->

                            bottomAppBarItems.find { it.destination.route == destination.route }
                        } ?: bottomAppBarItems.first()

                        mutableStateOf(item)
                    }

                    val containsInBottomAppBarItems = currentDestination?.let { destination ->
                        bottomAppBarItems.find {
                            it.destination.route == destination.route
                        }
                    } != null

                    val isShowFab = when(currentDestination?.route){
                        AppDestinations.Menu.route,AppDestinations.Drinks.route -> true
                        else -> false
                    }

                    PanucciApp(
                        bottomAppBarItemSelected = selectedItem,
                        onBottomAppBarItemSelectedChange = {
                            val route = it.destination.route
                            navController.navigate(route) {
                                launchSingleTop = true
                                popUpTo(route)
                            }
                        },
                        onFabClick = {
                            navController.navigate(AppDestinations.Checkout.route)
                        },
                        isShowTopBar = containsInBottomAppBarItems,
                        isShowBottomAppBar = containsInBottomAppBarItems,
                        isShowFab = isShowFab
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = AppDestinations.Highlights.route
                        ) {
                            composable(AppDestinations.Highlights.route) {
                                HighlightsListScreen(
                                    products = sampleProducts,
                                    onNavigateToDetails = {p: Product ->
                                        val promoCode = "ALURA"
                                        navController.navigate("${AppDestinations.ProductDetails.route}/${p.id}?promoCode=${promoCode}")
                                    },
                                    onNavigateToCheckout = {
                                        navController.navigate(AppDestinations.Checkout.route)
                                    },

                                    )
                            }
                            composable(AppDestinations.Menu.route) {
                                MenuListScreen(
                                    products = sampleProducts,
                                    onNavigateToDetails = {p: Product ->
                                        navController.navigate("${AppDestinations.ProductDetails.route}/${p.id}")
                                    }
                                )
                            }
                            composable(AppDestinations.Drinks.route) {
                                DrinksListScreen(
                                    products = sampleProducts,
                                    onNavigateToDetails = {p: Product ->
                                        navController.navigate("${AppDestinations.ProductDetails.route}/${p.id}")
                                    }
                                )
                            }

                            composable(
                                "${AppDestinations.ProductDetails.route}/{productId}?promoCode={promoCode}",
                                arguments = listOf(navArgument("promoCode"){nullable=true})
                            ) {backStackEntry ->
                                val id = backStackEntry.arguments?.getString("productId")
                                println(id)

                                val promoCode = backStackEntry.arguments?.getString("promoCode")



                                sampleProducts.find { id == it.id }?.let {product->

                                    val discount = when(promoCode){
                                        "ALURA" ->BigDecimal("0.1")
                                        else->BigDecimal.ZERO
                                    }
                                    val currentPrice = product.price

                                    ProductDetailsScreen(
                                        product = product.copy(price = currentPrice-(currentPrice*discount)),
                                        onNavigateToCheckout = {
                                            navController.navigate(AppDestinations.Checkout.route)
                                        }
                                    )
                                }

                            }
                            composable(AppDestinations.Checkout.route) {
                                CheckoutScreen(
                                    products = sampleProducts
                                )
                            }
                        }
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