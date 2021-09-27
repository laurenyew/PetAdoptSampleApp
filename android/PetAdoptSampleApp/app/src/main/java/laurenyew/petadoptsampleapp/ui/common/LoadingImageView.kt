package laurenyew.petadoptsampleapp.ui.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import laurenyew.petadoptsampleapp.R

@Composable
fun LoadingImageView(
    imageUrl: String? = null,
    modifier: Modifier = Modifier
) {
    if (imageUrl != null) {
        val painter = rememberImagePainter(data = imageUrl,
            builder = {
                error(R.drawable.ic_baseline_broken_image_24)
                fallback(R.drawable.ic_baseline_image_24)
                allowConversionToBitmap(true)
                transformations(RoundedCornersTransformation())
            })
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = modifier
        )

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