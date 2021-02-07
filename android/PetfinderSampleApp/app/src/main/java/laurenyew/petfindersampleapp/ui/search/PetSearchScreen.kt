package laurenyew.petfindersampleapp.ui.search

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ProgressBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import laurenyew.petfindersampleapp.R
import laurenyew.petfindersampleapp.repository.models.AnimalModel

@Composable
fun PetSearchScreen(viewModel: PetSearchViewModel = viewModel()) {
    val animalsState = viewModel.animals.observeAsState(initial = emptyList())
    val locationState = viewModel.location
    val isLoading = viewModel.isLoading.observeAsState(initial = false)
    val isError = viewModel.isError.observeAsState(false)

    Column {
        PetSearchBar(
            searchState = locationState,
            onSearch = { newLocation ->
                locationState.value = newLocation
                viewModel.searchAnimals()
            }
        )
        Spacer(Modifier.height(10.dp))
        PetSearchList(animals = animalsState,
            onItemClicked = { viewModel.openAnimalDetail(it) }
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
fun PetSearchBar(searchState: State<String>, onSearch: (String) -> Unit) {
    Row(modifier = Modifier.padding(10.dp)) {
        Text(
            text = stringResource(id = R.string.search_title),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(10.dp))
        TextField(
            value = searchState.value,
            onValueChange = onSearch,
            placeholder = {
                Text(stringResource(id = R.string.search_hint))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            onImeActionPerformed = { _, softwareController ->
                onSearch(searchState.value)
                softwareController?.hideSoftwareKeyboard()
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun PetSearchBar() {
    val searchState = mutableStateOf("78759")
    PetSearchBar(searchState = searchState, onSearch = { })
}

@Composable
fun PetSearchList(animals: State<List<AnimalModel>>, onItemClicked: (id: String) -> Unit) {
    val items = animals.value ?: emptyList()
    LazyColumn {
        items(items) { item ->
            val animalImageState = loadPicture(url = item.photoUrl)
            PetSearchListItem(
                item = item,
                imageState = animalImageState,
                onItemClicked = { id -> onItemClicked(id) })
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
fun PetSearchListItem(
    item: AnimalModel,
    imageState: State<ImageState>,
    onItemClicked: (id: String) -> Unit
) {
    val unknown = stringResource(id = R.string.unknown)
    val age = item.age ?: unknown
    val sex = item.sex ?: unknown
    val size = item.size ?: unknown
    val basicInfo = stringResource(id = R.string.basic_info_formatted_string, age, sex, size)
    val imageModifier = Modifier
        .preferredSize(72.dp)
        .padding(2.dp)
        .fillMaxSize()
    val loadedImageState = imageState.value
    Row(
        modifier = Modifier
            .wrapContentSize()
            .clickable(onClick = { onItemClicked(item.id) })
    ) {
        when (loadedImageState) {
            is ImageState.Success -> Image(
                bitmap = loadedImageState.image.asImageBitmap(),
                contentScale = ContentScale.Fit,
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
            )
            ImageState.Failed -> Image(
                imageVector = vectorResource(id = R.drawable.ic_baseline_broken_image_24),
                contentScale = ContentScale.Fit,
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
            )
            ImageState.Loading -> CircularProgressIndicator(
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
            )
            ImageState.Empty -> Image(
                imageVector = vectorResource(id = R.drawable.ic_baseline_image_24),
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
            Text(item.name ?: unknown, style = TextStyle(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(3.dp))
            Text(basicInfo)
            item.description?.let { description ->
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Preview
@Composable
fun PetSearchListItem() {
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
        mutableStateOf(ImageState.Loading)

    PetSearchListItem(
        item = animalModel,
        imageState = imageState,
        onItemClicked = { }
    )
}

@Composable
fun loadPicture(url: String?): State<ImageState> {
    val imageState: MutableState<ImageState> = mutableStateOf(ImageState.Empty)
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