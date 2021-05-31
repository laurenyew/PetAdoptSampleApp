package laurenyew.petadoptsampleapp.ui.features.list

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import laurenyew.petadoptsampleapp.R
import laurenyew.petadoptsampleapp.database.animal.Animal
import laurenyew.petadoptsampleapp.ui.theme.dividerColor

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
                imageState = animalImageState,
                onItemClicked = { id -> onItemClicked(id) },
                onItemFavorited = { isFavorited ->
                    onItemFavorited(item, isFavorited)
                }
            )
            Divider(color = dividerColor)
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
    item: Animal,
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
            .clickable(onClick = { onItemClicked(item.animalId) })
    ) {
        when (loadedImageState) {
            is ImageState.Success -> Image(
                bitmap = loadedImageState.image.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
            )
            ImageState.Failed -> Image(
                painter = painterResource(id = R.drawable.ic_baseline_broken_image_24),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
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
                contentScale = ContentScale.FillBounds,
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