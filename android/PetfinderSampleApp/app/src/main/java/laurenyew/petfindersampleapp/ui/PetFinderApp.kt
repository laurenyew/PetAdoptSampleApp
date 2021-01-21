package laurenyew.petfindersampleapp

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import laurenyew.petfindersampleapp.ui.favorites.PetFavoritesScreen
import laurenyew.petfindersampleapp.ui.search.PetSearchScreen

enum class Tab {
    SEARCH, FAVORITES
}

@Composable
fun PetFinderApp() {
    val selectedTab: MutableState<Tab> = mutableStateOf(Tab.SEARCH)

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            },
            bodyContent = {
                PetFinderContent(selectedTab = selectedTab.value)
            },
            bottomBar = {
                PetFinderBottomBar(
                    selectedTab = selectedTab,
                    onTabSelected = { newTab -> selectedTab.value = newTab }
                )
            }
        )
    }
}

@Composable
fun PetFinderBottomBar(selectedTab: State<Tab>, onTabSelected: (Tab) -> Unit) {
    BottomNavigation(backgroundColor = MaterialTheme.colors.primary) {
        BottomNavigationItem(
            icon = {
                Icon(imageVector = vectorResource(id = R.drawable.ic_baseline_search_24))
            },
            selected = selectedTab.value == Tab.SEARCH,
            onClick = { onTabSelected(Tab.SEARCH) },
            label = { Text(stringResource(id = R.string.title_pet_search_page)) }
        )
        BottomNavigationItem(
            icon = {
                Icon(imageVector = vectorResource(id = R.drawable.ic_baseline_favorite_24))
            },
            selected = selectedTab.value == Tab.FAVORITES,
            onClick = { onTabSelected(Tab.FAVORITES) },
            label = { Text(stringResource(id = R.string.title_favorites)) }
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
