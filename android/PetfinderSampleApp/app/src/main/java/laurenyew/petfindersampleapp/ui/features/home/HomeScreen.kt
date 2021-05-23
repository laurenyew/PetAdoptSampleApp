package laurenyew.petfindersampleapp.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import laurenyew.petfindersampleapp.R
import laurenyew.petfindersampleapp.ui.theme.dividerColor

@Composable
fun HomeScreen() {
    Column(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = "App icon",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        )
        Divider(color = dividerColor)
        Text(
            text = "Welcome to the sample PetFinder App!",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(8.dp)
        )
        Text(
            text = "This sample app uses PetFinder APIs to demo Jetpack libraries, Kotlin Coroutines & Flows, and other Android tech / architecture.",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(8.dp)
        )
        Text(
            text = "Feel free to browse the top drawer to see all the features!",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(8.dp)
        )

    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}