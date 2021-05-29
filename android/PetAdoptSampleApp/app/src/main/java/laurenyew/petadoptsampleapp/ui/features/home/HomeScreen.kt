package laurenyew.petadoptsampleapp.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import laurenyew.petadoptsampleapp.BuildConfig
import laurenyew.petadoptsampleapp.R
import laurenyew.petadoptsampleapp.ui.theme.dividerColor
import laurenyew.petadoptsampleapp.utils.collectAsStateLifecycleAware

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = viewModel()) {

    val lastSearchZipCode = viewModel.lastSearchTerm.collectAsStateLifecycleAware(initial = "")

    Column(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = "App icon",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        )
        Divider(color = dividerColor)
        if (lastSearchZipCode.value.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your last search zipcode: ${lastSearchZipCode.value}",
                modifier = Modifier
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = dividerColor)
        }
        Text(
            text = "Welcome to the sample PetAdopt App!",
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

        if (BuildConfig.CLIENT_ID == "null" || BuildConfig.CLIENT_SECRET == "null") {
            Text(
                text = "WARNING! You don't have your Petfinder client.id or client.secret set up in your local.properties. This app will not function until you do so. Please see the README.md for details. Thanks!",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(8.dp),
                color = Color.Red
            )
        }
    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}