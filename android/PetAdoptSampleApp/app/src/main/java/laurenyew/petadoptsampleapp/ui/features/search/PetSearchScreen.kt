package laurenyew.petadoptsampleapp.ui.features.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import laurenyew.petadoptsampleapp.R
import laurenyew.petadoptsampleapp.ui.features.list.PetList
import laurenyew.petadoptsampleapp.ui.theme.dividerColor
import laurenyew.petadoptsampleapp.ui.theme.sectionHeader
import laurenyew.petadoptsampleapp.utils.collectAsStateLifecycleAware
import timber.log.Timber

@Composable
fun PetSearchScreen(
    viewModel: PetSearchViewModel = hiltViewModel()
) {
    val animalsState = viewModel.animals.collectAsStateLifecycleAware(initial = emptyList())
    val locationState = viewModel.location
    val isLoading = viewModel.isLoading.collectAsStateLifecycleAware(initial = false)
    val isError = viewModel.isError.collectAsStateLifecycleAware(false)

    Column {
        PetSearchParamsCard(
            locationState = locationState,
            onLocationStateChanged = { newLocation ->
                locationState.value = newLocation
            },
            onExecuteSearch = {
                viewModel.searchAnimals()
            }
        )
        if (isLoading.value) {
            CircularProgressIndicator(
                modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }
        if (isError.value && !isLoading.value) {
            Text(
                text = stringResource(id = R.string.empty_results),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }
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
    }
}

@Composable
fun PetSearchParamsCard(
    locationState: State<String>,
    onLocationStateChanged: (String) -> Unit,
    onExecuteSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Search for available pets.",
            style = MaterialTheme.typography.sectionHeader(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        )
        PetSearchBar(
            searchState = locationState,
            onSearchFieldChange = onLocationStateChanged,
            onExecuteSearch = onExecuteSearch
        )
        Spacer(Modifier.height(10.dp))
        Divider(color = dividerColor)
    }
}

@Preview
@Composable
fun PetSearchParamsCardPreview() {
    val locationState = remember {
        mutableStateOf("78759")
    }
    PetSearchParamsCard(locationState = locationState,
        onLocationStateChanged = { locationState.value = it },
        onExecuteSearch = {
            Timber.d("Executing search")
        })
}