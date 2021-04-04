package laurenyew.petfindersampleapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import laurenyew.petfindersampleapp.ui.favorites.PetFavoritesScreen
import laurenyew.petfindersampleapp.ui.search.PetSearchScreen

enum class Tab {
    SEARCH, FAVORITES
}

@Composable
fun PetFinderApp() {
    val scaffoldState = rememberScaffoldState()
    val drawerState = scaffoldState.drawerState
    val selectedTab: MutableState<Tab> = mutableStateOf(Tab.SEARCH)

    MaterialTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar {
                    Icon(imageVector = Icons.Default.Menu,
                        null,
                        modifier = Modifier.clickable {
                            runBlocking { drawerState.open() }
                        })
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            },
            drawerContent = {
                PetFinderDrawer(
                    onTabSelected = { newTab ->
                        selectedTab.value = newTab
                        runBlocking { drawerState.close() }
                    }
                )
            },
            drawerGesturesEnabled = true,
            content = {
                PetFinderContent(selectedTab = selectedTab.value)
            }
        )
    }
}

@Composable
fun PetFinderDrawer(onTabSelected: (Tab) -> Unit) {
    Column {
        Button(
            onClick = { onTabSelected(Tab.SEARCH) },
            content = {
                Text(stringResource(id = R.string.title_pet_search_page))
            }
        )
        Button(
            onClick = { onTabSelected(Tab.FAVORITES) },
            content = {
                Text(stringResource(id = R.string.title_favorites))
            }
        )
    }
}

@Composable
fun PetFinderContent(selectedTab: Tab) {
    when (selectedTab) {
        Tab.SEARCH -> PetSearchScreen()
        Tab.FAVORITES -> PetFavoritesScreen()
    }
}

@Preview
@Composable
fun PetFinderMainScreenDemo() {
    PetFinderApp()
}
