package laurenyew.petadoptsampleapp.ui.features.list

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import laurenyew.petadoptsampleapp.ui.common.FavoriteButton
import laurenyew.petadoptsampleapp.ui.common.LoadingImageView

@Composable
fun ListItem(
    imageUrl: String?,
    title: String,
    description: String,
    isFavorite: State<Boolean>? = null,
    onItemFavorited: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        val imageModifier = Modifier
            .size(72.dp)
            .padding(2.dp)
            .fillMaxSize()

        LoadingImageView(imageUrl, imageModifier)
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
            FavoriteButton(
                isFavorite = isFavorite.value,
                onItemFavorited = onItemFavorited,
                modifier = imageModifier
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}