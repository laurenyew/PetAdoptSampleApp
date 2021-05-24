package laurenyew.petfindersampleapp.ui.features.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import laurenyew.petfindersampleapp.R
import laurenyew.petfindersampleapp.ui.features.list.PetList
import laurenyew.petfindersampleapp.ui.features.search.PetSearchBar
import laurenyew.petfindersampleapp.ui.theme.sectionHeader

@Composable
fun PetFavoritesScreen(viewModel: FavoritesViewModel = viewModel()) {
    val animalsState = viewModel.animals.collectAsState(initial = emptyList())
    val isLoading = viewModel.isLoading.collectAsState(initial = false)
    val isError = viewModel.isError.collectAsState(false)

    Column {
        Text(
            text = "Favorite Pets List",
            style = MaterialTheme.typography.sectionHeader(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        )
        Spacer(Modifier.height(10.dp))
        PetList(animals = animalsState,
            onItemClicked = { viewModel.openAnimalDetail(it) },
            onItemFavorited = { item, isFavorited ->
                if (isFavorited) {
                    viewModel.favorite(item)
                } else {
                    viewModel.unfavorite(item.id)
                }
            }
        )
        if (isLoading.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        if (isError.value) {
            Text(
                text = stringResource(id = R.string.empty_results),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
fun PetFavoritesScreenPreview(){
    PetFavoritesScreen()
}