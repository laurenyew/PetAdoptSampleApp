package laurenyew.petadoptsampleapp.ui.features.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import laurenyew.petadoptsampleapp.R
import laurenyew.petadoptsampleapp.ui.theme.sectionHeader

@Composable
fun PetFiltersCard(
    favoritesFilterState: State<FavoritesFilter>,
    onUpdateFavoritesFilter: (FavoritesFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val showExpandedState = remember {
        mutableStateOf(false)
    }
    val favoritesFilter = favoritesFilterState.value
    var showDogs = remember(favoritesFilter) {
        mutableStateOf(favoritesFilter.showDogs)
    }
    var showCats = remember(favoritesFilter) {
        mutableStateOf(favoritesFilter.showCats)
    }
    var showFemales = remember(favoritesFilter) {
        mutableStateOf(favoritesFilter.showFemales)
    }
    var showMales = remember(favoritesFilter) {
        mutableStateOf(favoritesFilter.showMales)
    }

    Column(modifier = modifier.padding(8.dp)) {
        Row {
            Text(
                text = "Filter Your Favorites",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(8.dp)
            )
            if (showExpandedState.value) {
                Image(
                    painter = painterResource(id = R.drawable.ic_expand_less),
                    contentDescription = "Collapse Filters",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            showExpandedState.value = false
                        }
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_expand_more),
                    contentDescription = "Expand Filters",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            showExpandedState.value = true
                        }
                )
            }
        }
        if (showExpandedState.value) {
            Text(
                text = "Animal Type:",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(8.dp)
            )
            PetFilterCheckbox(
                text = "Show Dogs",
                checkedState = showDogs.value,
                onCheckedChange = { showDogs.value = it },
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            )
            PetFilterCheckbox(
                text = "Show Cats",
                checkedState = showCats.value,
                onCheckedChange = { showCats.value = it },
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            )
            Text(
                text = "Gender:",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(8.dp)
            )

            PetFilterCheckbox(
                text = "Show Females",
                checkedState = showFemales.value,
                onCheckedChange = { showFemales.value = it },
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            )
            PetFilterCheckbox(
                text = "Show Males",
                checkedState = showMales.value,
                onCheckedChange = { showMales.value = it },
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                onUpdateFavoritesFilter(
                    favoritesFilter.copy(
                        showDogs = showDogs.value,
                        showCats = showCats.value,
                        showFemales = showFemales.value,
                        showMales = showMales.value
                    )
                )
            }) {
                Text(
                    "Apply Filters",
                    style = MaterialTheme.typography.button
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun PetFilterCheckbox(
    text: String,
    checkedState: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .align(Alignment.CenterVertically)
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
        checkedState = checkedState.value,
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