package laurenyew.petadoptsampleapp.ui.features.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import laurenyew.petadoptsampleapp.R
import laurenyew.petadoptsampleapp.repository.PetFavoriteRepository
import laurenyew.petadoptsampleapp.ui.features.list.PetList
import laurenyew.petadoptsampleapp.ui.theme.dividerColor
import laurenyew.petadoptsampleapp.ui.theme.sectionHeader
import laurenyew.petadoptsampleapp.utils.collectAsStateLifecycleAware

@Composable
fun PetFavoritesScreen(viewModel: FavoritesViewModel = viewModel()) {
    val animalsState = viewModel.animals.collectAsStateLifecycleAware(emptyList())
    val isLoading = viewModel.isLoading.collectAsStateLifecycleAware(false)
    val isError = viewModel.isError.collectAsStateLifecycleAware(false)

    val filterState =
        viewModel.filterState.collectAsStateLifecycleAware(
            initial = PetFavoriteRepository.DEFAULT_FAVORITES_FILTER
        )
    Column {
        Surface(
            elevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            PetFiltersCard(
                favoritesFilterState = filterState,
                onUpdateFavoritesFilter = {
                    viewModel.updateFilterState(it)
                }
            )
        }
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
                    viewModel.unfavorite(item.animalId)
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
fun PetFavoritesScreenPreview() {
    PetFavoritesScreen()
}