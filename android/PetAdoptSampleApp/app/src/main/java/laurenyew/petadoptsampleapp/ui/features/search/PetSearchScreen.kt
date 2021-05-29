package laurenyew.petadoptsampleapp.ui.features.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import laurenyew.petadoptsampleapp.R
import laurenyew.petadoptsampleapp.ui.features.list.PetList
import laurenyew.petadoptsampleapp.ui.theme.sectionHeader
import laurenyew.petadoptsampleapp.utils.collectAsStateLifecycleAware

@Composable
fun PetSearchScreen(
    viewModel: PetSearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val animalsState = viewModel.animals.collectAsStateLifecycleAware(initial = emptyList())
    val locationState = viewModel.location
    val isLoading = viewModel.isLoading.collectAsStateLifecycleAware(initial = false)
    val isError = viewModel.isError.collectAsStateLifecycleAware(false)

    Column {
        Text(
            text = "Search for available pets.",
            style = MaterialTheme.typography.sectionHeader(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        )
        PetSearchBar(
            searchState = locationState,
            onSearchFieldChange = { newLocation ->
                locationState.value = newLocation
            },
            onExecuteSearch = {
                viewModel.searchAnimals()
            }
        )
        Spacer(Modifier.height(10.dp))
        if (isLoading.value) {
            CircularProgressIndicator(
                modifier =
                Modifier.align(Alignment.CenterHorizontally)
            )
        }
        if (isError.value && !isLoading.value) {
            Text(
                text = stringResource(id = R.string.empty_results),
                modifier = Modifier.align(Alignment.CenterHorizontally)
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
fun PetSearchBar(
    searchState: State<String>,
    onSearchFieldChange: (String) -> Unit,
    onExecuteSearch: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Row(modifier = Modifier.padding(10.dp)) {
        Text(
            text = stringResource(id = R.string.search_title),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(10.dp))
        TextField(
            value = searchState.value,
            onValueChange = onSearchFieldChange,
            placeholder = {
                Text(stringResource(id = R.string.search_hint))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                onExecuteSearch()
                focusManager.clearFocus()
            }),
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun PetSearchBarPreview() {
    val searchState = remember { mutableStateOf("78759") }
    PetSearchBar(searchState = searchState, onSearchFieldChange = {}, onExecuteSearch = {})
}