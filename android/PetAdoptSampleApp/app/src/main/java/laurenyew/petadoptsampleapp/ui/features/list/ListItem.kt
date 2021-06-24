package laurenyew.petadoptsampleapp.ui.features.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.imageloading.ImageLoadState
import laurenyew.petadoptsampleapp.R

@Composable
fun ListItem(
    imageUrl: String?,
    title: String,
    description: String,
    isFavorite: State<Boolean>? = null,
    onItemFavorited: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val imageModifier = Modifier
        .size(72.dp)
        .padding(2.dp)
        .fillMaxSize()

    val painter = rememberGlidePainter(imageUrl)

    Row(modifier = modifier) {
        when (painter.loadState) {
            is ImageLoadState.Success -> Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
            )
            is ImageLoadState.Error -> Image(
                painter = painterResource(id = R.drawable.ic_baseline_broken_image_24),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
            )
            is ImageLoadState.Loading -> CircularProgressIndicator(
                modifier = imageModifier
                    .align(Alignment.CenterVertically)
            )
            is ImageLoadState.Empty -> Image(
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
            Text(title, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(3.dp))
            Text(description)
        }

        if (isFavorite != null) {
            Spacer(modifier = Modifier.width(5.dp))
            if (isFavorite.value) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_favorite_24),
                    contentDescription = "Favorite-d",
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primaryVariant),
                    modifier = imageModifier
                        .width(8.dp)
                        .height(8.dp)
                        .align(Alignment.CenterVertically)
                        .clickable { onItemFavorited?.invoke(false) }
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
                        .clickable { onItemFavorited?.invoke(true) }
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}