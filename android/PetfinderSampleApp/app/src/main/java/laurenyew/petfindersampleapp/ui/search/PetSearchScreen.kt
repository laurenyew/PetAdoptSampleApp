package laurenyew.petfindersampleapp.ui.search

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
    }
}

@Composable
fun PetSearchBar(searchState: State<String>, onSearch: (String) -> Unit) {
    Column(modifier = Modifier.padding(2.dp)) {
        Text(
            text = stringResource(id = R.string.search_title),
            style = MaterialTheme.typography.subtitle1
        )
        Row {
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
                onImeActionPerformed = { _, _ ->
                    onSearch(searchState.value)
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(onClick = { onSearch(searchState.value) }) {
                Icon(
                    imageVector = vectorResource(id = R.drawable.ic_baseline_search_24),
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
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
            val animalImage = loadPicture(url = item.photoUrl)
            PetSearchListItem(
                item = item,
                image = animalImage,
                onItemClicked = { id -> onItemClicked(id) })
        }
    }
}


@Composable
fun PetSearchListItem(
    item: AnimalModel,
    image: State<Bitmap?>,
    onItemClicked: (id: String) -> Unit
) {
    val unknown = stringResource(id = R.string.unknown)
    val age = item.age ?: unknown
    val sex = item.sex ?: unknown
    val size = item.size ?: unknown
    val basicInfo = stringResource(id = R.string.basic_info_formatted_string, age, sex, size)
    val imageBitmap = remember { image.value }
    val imageModifier = Modifier
        .preferredSize(72.dp)
        .fillMaxSize()

    Card(
        border = BorderStroke(.05.dp, Color.DarkGray),
        modifier = Modifier.clickable(onClick = { onItemClicked(item.id) })
    ) {
        Row(modifier = Modifier.wrapContentSize()) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentScale = ContentScale.Fit,
                    modifier = imageModifier
                )
            } else {
                Image(
                    imageVector = vectorResource(id = R.drawable.ic_baseline_image_24),
                    contentScale = ContentScale.Fit,
                    modifier = imageModifier
                )
            }


            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(item.name ?: unknown)
                Spacer(modifier = Modifier.height(3.dp))
                Text(basicInfo)
                Spacer(modifier = Modifier.height(3.dp))
                Text(item.description ?: unknown)
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
        description = "A Good Dog",
        status = "Ready for Adoption",
        breed = "Mixed",
        photoUrl = null,
        distance = null,
        contact = null,
        orgId = null
    )
    val imageState: State<Bitmap?> =
        mutableStateOf(null)

    PetSearchListItem(
        item = animalModel,
        image = imageState,
        onItemClicked = { }
    )
}

@Composable
fun loadPicture(url: String?): State<Bitmap?> {
    val bitmapState: MutableState<Bitmap?> = mutableStateOf(null)
    Picasso.get()
        .load(url)
        .into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmapState.value = bitmap
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                bitmapState.value = null
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                bitmapState.value = null
            }

        })

    return bitmapState
}