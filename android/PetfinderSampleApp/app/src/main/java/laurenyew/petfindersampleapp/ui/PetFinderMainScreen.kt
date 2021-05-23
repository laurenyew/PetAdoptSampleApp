package laurenyew.petfindersampleapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import laurenyew.petfindersampleapp.R
import laurenyew.petfindersampleapp.ui.features.favorites.PetFavoritesScreen
import laurenyew.petfindersampleapp.ui.features.home.HomeScreen
import laurenyew.petfindersampleapp.ui.features.search.PetSearchScreen
import laurenyew.petfindersampleapp.ui.features.search.PetSearchViewModel
import laurenyew.petfindersampleapp.ui.theme.PetfinderTheme


@Composable
fun PetFinderMainScreen() {
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    val drawerState = scaffoldState.drawerState
    val openDrawer: () -> Unit = {
        scope.launch {
            drawerState.open()
        }
    }


    PetfinderTheme {
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
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            },
            drawerContent = {
                PetFinderDrawer(
                    onScreenSelected = { route ->
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
                PetFinderMainContent(
                    navController = navController,
                )
            }
        )
    }
}

@Composable
fun PetFinderMainContent(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = DrawerScreens.Home.route
    ) {
        composable(DrawerScreens.Home.route) {
            HomeScreen()
        }
        composable(DrawerScreens.Search.route) {
            val searchViewModel: PetSearchViewModel = hiltNavGraphViewModel()
            PetSearchScreen(
                viewModel = searchViewModel
            )
        }
        composable(DrawerScreens.Favorites.route) {
            PetFavoritesScreen()
        }
    }
}

@Preview
@Composable
fun PetFinderMainScreenPreview() {
    PetFinderMainScreen()
}
