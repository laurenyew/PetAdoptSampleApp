package laurenyew.petadoptsampleapp.ui.features.favorites

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import laurenyew.petadoptsampleapp.R
import laurenyew.petadoptsampleapp.data.PetFavoriteRepository
import laurenyew.petadoptsampleapp.ui.features.petList.PetList
import laurenyew.petadoptsampleapp.ui.theme.sectionHeader
import laurenyew.petadoptsampleapp.utils.collectAsStateLifecycleAware

@Composable
fun PetFavoritesScreen(viewModel: FavoritesViewModel = viewModel()) {
    val animalsState = viewModel.animals.collectAsStateLifecycleAware(emptyList())
    val isLoading = viewModel.isLoading.collectAsStateLifecycleAware(false)
    val isError = viewModel.isError.collectAsStateLifecycleAware(false)
    val errorState = viewModel.errorState.collectAsStateLifecycleAware(initial = null)
    errorState.value?.let { message ->
        val context = LocalContext.current
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    val context = LocalContext.current

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
        PetList(
            isRefreshing = isLoading,
            animals = animalsState,
            onRefresh = { viewModel.refreshFavorites() },
            onItemClicked = { viewModel.openAnimalDetail(context, it) },
            onItemFavorited = { item, isFavorited ->
                if (isFavorited) {
                    viewModel.favorite(item)
                } else {
                    viewModel.unfavorite(item.animalId)
                }
            }
        )
        if (animalsState.value.isEmpty() && filterState.value.isFiltering()) {
            Text(
                "Filters are applied. You may have filtered out all your data!",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }
        if (isError.value) {
            Text(
                text = stringResource(id = R.string.empty_results),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }

    }
}

@Preview
@Composable
fun PetFavoritesScreenPreview() {
    PetFavoritesScreen()
}