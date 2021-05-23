package laurenyew.petfindersampleapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import laurenyew.petfindersampleapp.R
import laurenyew.petfindersampleapp.ui.theme.dividerColor

sealed class DrawerScreens(val route: String) {
    object Home : DrawerScreens("Home")
    object Search : DrawerScreens("Search")
    object Favorites : DrawerScreens("Favorites")
}

private val screens = listOf(
    DrawerScreens.Home,
    DrawerScreens.Search,
    DrawerScreens.Favorites
)

@Composable
fun PetFinderDrawer(
    onScreenSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = "App icon",
            modifier = Modifier.fillMaxWidth()
        )
        Divider(color = dividerColor)
        screens.forEach { screen ->
            Spacer(Modifier.height(24.dp))
            Text(
                text = screen.route,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.clickable {
                    onScreenSelected(screen.route)
                }
            )
        }
    }
}

@Preview
@Composable
fun PetFinderDrawerPreview() {
    PetFinderDrawer(onScreenSelected = {
        // Do nothing
    })
}