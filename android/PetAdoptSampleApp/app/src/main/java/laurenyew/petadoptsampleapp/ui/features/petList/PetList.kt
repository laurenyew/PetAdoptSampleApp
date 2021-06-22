package laurenyew.petadoptsampleapp.ui.features.petList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import laurenyew.petadoptsampleapp.R
import laurenyew.petadoptsampleapp.database.animal.Animal
import laurenyew.petadoptsampleapp.ui.features.list.ListItem
import laurenyew.petadoptsampleapp.ui.images.ImageState
import laurenyew.petadoptsampleapp.ui.images.loadPicture
import laurenyew.petadoptsampleapp.ui.theme.dividerColor
import laurenyew.petadoptsampleapp.utils.collectAsStateLifecycleAware

@Composable
fun PetList(
    animals: State<List<Animal>>,
    onItemClicked: (id: String) -> Unit,
    onItemFavorited: (item: Animal, isFavorited: Boolean) -> Unit
) {
    val items = animals.value
    LazyColumn {
        items(items.size) { index ->
            val item = items[index]
            val animalImageState = loadPicture(url = item.photoUrl)
            PetListItem(
                item = item,
                imageState = animalImageState.collectAsStateLifecycleAware(initial = ImageState.Empty),
                onItemClicked = { id -> onItemClicked(id) },
                onItemFavorited = { isFavorited ->
                    onItemFavorited(item, isFavorited)
                }
            )
            Divider(color = dividerColor)
        }
    }
}


@Composable
fun PetListItem(
    item: Animal,
    imageState: State<ImageState>,
    onItemClicked: (String) -> Unit,
    onItemFavorited: (Boolean) -> Unit
) {
    val unknown = stringResource(id = R.string.unknown)
    val age = item.age ?: unknown
    val sex = item.sex ?: unknown
    val size = item.size ?: unknown
    val title = item.name ?: unknown
    val description = stringResource(id = R.string.basic_info_formatted_string, age, sex, size)
    val isFavorite = remember(item) {
        mutableStateOf(item.isFavorite)
    }

    ListItem(
        imageState = imageState,
        title = title, description = description,
        isFavorite = isFavorite,
        onItemFavorited = onItemFavorited,
        modifier = Modifier
            .clickable(onClick = { onItemClicked(item.animalId) })
    )
}


@Preview
@Composable
fun PetListItemPreview() {
    val animalModel = Animal(
        animalId = "testId", name = "Fido", species = "Dog",
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