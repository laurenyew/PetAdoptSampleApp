package laurenyew.petadoptsampleapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import laurenyew.petadoptsampleapp.R
import laurenyew.petadoptsampleapp.ui.theme.dividerColor
import laurenyew.petadoptsampleapp.ui.theme.logoColor

sealed class DrawerScreens(val route: String) {
    object Home : DrawerScreens("Home")
    object Search : DrawerScreens("Search")
    object Favorites : DrawerScreens("Favorites")
}

private val screens = listOf(
    DrawerScreens.Home,
    DrawerScreens.Search,
    DrawerScreens.Favorites
)

@Composable
fun PetAdoptDrawer(
    onScreenSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(top = 48.dp)
    ) {
        Row {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "App icon",
                contentScale = ContentScale.Fit,
            )
            Text(
                text = "PetAdopt",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.align(Alignment.CenterVertically),
                color = logoColor
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = dividerColor)
        screens.forEach { screen ->
            Spacer(Modifier.height(24.dp))
            Text(
                text = screen.route,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .padding(start = 24.dp)
                    .clickable {
                        onScreenSelected(screen.route)
                    }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        PetfinderCaptionText()
    }
}

@Preview
@Composable
fun PetAdoptDrawerPreview() {
    PetAdoptDrawer(onScreenSelected = {
        // Do nothing
    })
}

@Composable
fun PetfinderCaptionText(modifier: Modifier = Modifier) {
    val annotatedLinkString: AnnotatedString = buildAnnotatedString {

        val str = "Powered by Petfinder"
        val startIndex = str.indexOf("Petfinder")
        val endIndex = startIndex + 9
        append(str)
        addStyle(
            style = SpanStyle(
                color = logoColor,
                fontSize = 18.sp,
                textDecoration = TextDecoration.Underline
            ), start = startIndex, end = endIndex
        )

        // attach a string annotation that stores a URL to the text "link"
        addStringAnnotation(
            tag = "URL",
            annotation = "https://www.petfinder.com",
            start = startIndex,
            end = endIndex
        )

    }

    // UriHandler parse and opens URI inside AnnotatedString Item in Browse
    val uriHandler = LocalUriHandler.current

    // Clickable text returns position of text that is clicked in onClick callback
    ClickableText(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        text = annotatedLinkString,
        onClick = {
            annotatedLinkString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}

@Preview
@Composable
fun PetfinderCaptionTextPreview() {
    PetfinderCaptionText()
}