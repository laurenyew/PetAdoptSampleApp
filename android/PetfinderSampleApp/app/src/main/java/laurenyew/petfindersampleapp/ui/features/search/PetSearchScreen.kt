package laurenyew.petfindersampleapp.ui.features.search

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
import laurenyew.petfindersampleapp.R
import laurenyew.petfindersampleapp.repository.models.AnimalModel
import laurenyew.petfindersampleapp.ui.theme.sectionHeader

@Composable
fun PetSearchScreen(
    viewModel: PetSearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val animalsState = viewModel.animals.collectAsState(initial = emptyList())
    val locationState = viewModel.location
    val isLoading = viewModel.isLoading.collectAsState(initial = false)
    val isError = viewModel.isError.collectAsState(false)

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

@Composable
fun PetList(
    animals: State<List<AnimalModel>>,
    onItemClicked: (id: String) -> Unit,
    onItemFavorited: (item: AnimalModel, isFavorited: Boolean) -> Unit
) {
    val items = animals.value
    LazyColumn {
        items(items.size) { index ->
            val item = items[index]
            val animalImageState = loadPicture(url = item.photoUrl)
            PetListItem(
                item = item,
                imageState = animalImageState,
                onItemClicked = { id -> onItemClicked(id) },
                onItemFavorited = { isFavorited ->
                    onItemFavorited(item, isFavorited)
                }
            )
            Divider(color = Color.Black)
        }
    }
}

sealed class ImageState {
    data class Success(val image: Bitmap) : ImageState()
    object Failed : ImageState()
    object Loading : ImageState()
    object Empty : ImageState()
}

@Composable
fun PetListItem(
    item: AnimalModel,
    imageState: State<ImageState>,
    onItemClicked: (id: String) -> Unit,
    onItemFavorited: (isFavorited: Boolean) -> Unit
) {
    val unknown = stringResource(id = R.string.unknown)
    val age = item.age ?: unknown
    val sex = item.sex ?: unknown
    val size = item.size ?: unknown
    val basicInfo = stringResource(id = R.string.basic_info_formatted_string, age, sex, size)
    val imageModifier = Modifier
        .size(72.dp)
        .padding(2.dp)
        .fillMaxSize()
    val loadedImageState = imageState.value
    Row(
        modifier = Modifier
            .clickable(onClick = { onItemClicked(item.id) })
    ) {
        when (loadedImageState) {
            is ImageState.Success -> Image(
                bitmap = loadedImageState.image.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
            )
            ImageState.Failed -> Image(
                painter = painterResource(id = R.drawable.ic_baseline_broken_image_24),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
            )
            ImageState.Loading -> CircularProgressIndicator(
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
            )
            ImageState.Empty -> Image(
                painter = painterResource(id = R.drawable.ic_baseline_image_24),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(2.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(item.name ?: unknown, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(3.dp))
            Text(basicInfo)
        }
        Spacer(modifier = Modifier.width(5.dp))
        if (item.isFavorite) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_favorite_24),
                contentDescription = "Favorite-d",
                contentScale = ContentScale.Inside,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primaryVariant),
                modifier = imageModifier
                    .width(8.dp)
                    .height(8.dp)
                    .align(Alignment.CenterVertically)
                    .clickable { onItemFavorited(false) }
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_unfavorite_24),
                contentDescription = "Not Favorite-d",
                contentScale = ContentScale.Inside,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primaryVariant),
                modifier = imageModifier
                    .width(8.dp)
                    .height(8.dp)
                    .align(Alignment.CenterVertically)
                    .clickable { onItemFavorited(true) }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}


@Preview
@Composable
fun PetSearchListItemPreview() {
    val animalModel = AnimalModel(
        id = "testId", name = "Fido", type = "Dog",
        sex = "Male",
        age = "7 years",
        size = "Large",
        description = "A great dog is an amazing companion that loves to play ball and fetch and give kisses\nLoves to play ball",
        status = "Ready for Adoption",
        breed = "Mixed",
        photoUrl = null,
        distance = null,
        contact = null,
        orgId = null
    )
    val imageState: State<ImageState> =
        remember { mutableStateOf(ImageState.Loading) }

    PetListItem(
        item = animalModel,
        imageState = imageState,
        onItemClicked = { },
        onItemFavorited = {}
    )
}

@Composable
fun loadPicture(url: String?): State<ImageState> {
    val imageState: MutableState<ImageState> = remember { mutableStateOf(ImageState.Empty) }
    url?.let {
        Picasso.get()
            .load(url)
            .into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    if (bitmap != null) {
                        imageState.value = ImageState.Success(image = bitmap)
                    } else {
                        imageState.value = ImageState.Empty
                    }
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    imageState.value = ImageState.Failed
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    imageState.value = ImageState.Loading
                }

            })
    }
    return imageState
}