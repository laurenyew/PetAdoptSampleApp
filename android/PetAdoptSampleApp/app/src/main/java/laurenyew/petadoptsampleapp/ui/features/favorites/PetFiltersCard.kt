package laurenyew.petadoptsampleapp.ui.features.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PetFiltersCard(
    favoritesFilterState: State<FavoritesFilter>,
    onUpdateFavoritesFilter: (FavoritesFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoritesFilter = favoritesFilterState.value
    val showDogs = remember {
        mutableStateOf(favoritesFilter.typeFilter.showDogs)
    }
    val showCats = remember {
        mutableStateOf(favoritesFilter.typeFilter.showCats)
    }
    val showFemales = remember {
        mutableStateOf(favoritesFilter.genderFilter.showFemales)
    }
    val showMales = remember {
        mutableStateOf(favoritesFilter.genderFilter.showMales)
    }
    Column(modifier = modifier) {
        Text(
            text = "Filter Your Favorites",
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        PetFilterCheckbox(
            text = "Show Dogs",
            checkedState = showDogs,
            onCheckedChange = { showDogs.value = it },
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        PetFilterCheckbox(
            text = "Show Cats",
            checkedState = showCats,
            onCheckedChange = { showCats.value = it },
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        PetFilterCheckbox(
            text = "Show Females",
            checkedState = showFemales,
            onCheckedChange = { showFemales.value = it },
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        PetFilterCheckbox(
            text = "Show Males",
            checkedState = showMales,
            onCheckedChange = { showMales.value = it },
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            onUpdateFavoritesFilter(
                favoritesFilter.copy(
                    typeFilter = favoritesFilter.typeFilter.copy(
                        showDogs = showDogs.value,
                        showCats = showCats.value,
                    ),
                    genderFilter = favoritesFilter.genderFilter.copy(
                        showFemales = showFemales.value,
                        showMales = showMales.value
                    )
                )
            )
        }) {
            Text("Apply Filters")
        }
    }
}


@Composable
fun PetFilterCheckbox(
    text: String,
    checkedState: State<Boolean>,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
fun PetFilterCheckboxPreview() {
    val checkedState = remember {
        mutableStateOf(true)
    }
    PetFilterCheckbox(
        text = "Show Test",
        checkedState = checkedState,
        onCheckedChange = {
            checkedState.value = it
        })
}


@Preview
@Composable
fun PetFiltersCardPreview() {
    val favoritesFilter = remember {
        mutableStateOf(FavoritesFilter())
    }
    PetFiltersCard(favoritesFilterState = favoritesFilter,
        onUpdateFavoritesFilter = {
            favoritesFilter.value = it
        })
}