package laurenyew.petadoptsampleapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.ui.features.favorites.FavoritesViewModel
import laurenyew.petadoptsampleapp.ui.features.favorites.PetFavoritesScreen
import laurenyew.petadoptsampleapp.ui.features.home.HomeScreen
import laurenyew.petadoptsampleapp.ui.features.home.HomeScreenViewModel
import laurenyew.petadoptsampleapp.ui.features.search.PetSearchScreen
import laurenyew.petadoptsampleapp.ui.features.search.PetSearchViewModel
import laurenyew.petadoptsampleapp.ui.theme.PetAdoptTheme


@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    val drawerState = scaffoldState.drawerState
    val openDrawer: () -> Unit = {
        scope.launch {
            drawerState.open()
        }
    }

    val titleState = remember { mutableStateOf(DrawerScreens.Home.route) }


    PetAdoptTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar {
                    Icon(imageVector = Icons.Default.Menu,
                        null,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                openDrawer()
                            })
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = titleState.value,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            },
            drawerContent = {
                MainDrawer(
                    onScreenSelected = { route ->
                        titleState.value = route
                        scope.launch { drawerState.close() }
                        navController.navigate(route) {
                            popUpTo = navController.graph.startDestinationId
                            launchSingleTop = true
                        }
                    }
                )
            },
            drawerGesturesEnabled = true,
            content = {
                MainScreenContent(
                    navController = navController,
                )
            }
        )
    }
}

@Composable
fun MainScreenContent(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = DrawerScreens.Home.route
    ) {
        composable(DrawerScreens.Home.route) {
            val homeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(viewModel = homeScreenViewModel)
        }
        composable(DrawerScreens.Search.route) {
            val searchViewModel = hiltViewModel<PetSearchViewModel>()
            PetSearchScreen(
                viewModel = searchViewModel
            )
        }
        composable(DrawerScreens.Favorites.route) {
            val favoritesViewModel = hiltViewModel<FavoritesViewModel>()
            PetFavoritesScreen(favoritesViewModel)
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}
