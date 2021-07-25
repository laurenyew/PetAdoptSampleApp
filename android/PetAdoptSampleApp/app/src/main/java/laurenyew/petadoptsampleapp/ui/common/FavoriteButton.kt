package laurenyew.petadoptsampleapp.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import laurenyew.petadoptsampleapp.R

@Composable
fun FavoriteButton(
    isFavorite: Boolean = false,
    onItemFavorited: ((Boolean) -> Unit)? = null,
    modifier: Modifier =
        Modifier
            .width(8.dp)
            .height(8.dp)
) {
    if (isFavorite) {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_favorite_24),
            contentDescription = "Favorite-d",
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primaryVariant),
            modifier = modifier
                .clickable { onItemFavorited?.invoke(false) }
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_unfavorite_24),
            contentDescription = "Not Favorite-d",
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primaryVariant),
            modifier = modifier
                .width(8.dp)
                .height(8.dp)
                .clickable { onItemFavorited?.invoke(true) }
        )
    }
}

@Preview
@Composable
fun FavoriteButton_Preview() {
    FavoriteButton()
}