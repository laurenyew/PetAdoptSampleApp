package laurenyew.petadoptsampleapp.ui.images

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.runtime.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

sealed class ImageState {
    data class Success(val image: Bitmap) : ImageState()
    object Failed : ImageState()
    object Loading : ImageState()
    object Empty : ImageState()
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