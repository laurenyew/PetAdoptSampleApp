package laurenyew.petadoptsampleapp.ui.common

import androidx.compose.foundation.Image
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.imageloading.ImageLoadState
import laurenyew.petadoptsampleapp.R

@Composable
fun LoadingImageView(
    imageUrl: String? = null,
    modifier: Modifier = Modifier
) {
    if (imageUrl != null) {
        val painter = rememberGlidePainter(imageUrl)

        when (painter.loadState) {
            is ImageLoadState.Success -> Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = modifier
            )
            is ImageLoadState.Error -> Image(
                painter = painterResource(id = R.drawable.ic_baseline_broken_image_24),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = modifier
            )
            is ImageLoadState.Loading -> CircularProgressIndicator(
                modifier = modifier
            )
            is ImageLoadState.Empty -> Image(
                painter = painterResource(id = R.drawable.ic_baseline_image_24),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = modifier
            )
        }
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_image_24),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = modifier
        )
    }
}

@Preview
@Composable
fun LoadingImageView_Preview() {
    LoadingImageView()
}