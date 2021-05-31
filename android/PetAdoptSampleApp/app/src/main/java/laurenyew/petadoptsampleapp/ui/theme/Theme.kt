package laurenyew.petadoptsampleapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.CheckboxColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState

private val DarkColorPallette = darkColors(
    primary = purple200,
    primaryVariant = purple700,
    secondary = teal200
)

private val LightColorPallette = lightColors(
    primary = purple500,
    primaryVariant = purple700,
    secondary = teal200,
    surface = purple100
)

@Composable
fun PetAdoptTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPallette
    } else {
        LightColorPallette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}