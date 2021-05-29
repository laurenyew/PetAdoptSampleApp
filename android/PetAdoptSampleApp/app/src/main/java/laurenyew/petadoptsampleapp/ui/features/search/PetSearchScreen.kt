package laurenyew.petadoptsampleapp.ui.features.search

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import laurenyew.petadoptsampleapp.R
import laurenyew.petadoptsampleapp.repository.models.AnimalModel
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
                    viewModel.unfavorite(item.id)
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