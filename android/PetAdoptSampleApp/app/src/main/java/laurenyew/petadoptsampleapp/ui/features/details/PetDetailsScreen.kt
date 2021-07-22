package laurenyew.petadoptsampleapp.ui.features.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import laurenyew.petadoptsampleapp.database.animal.Animal
import laurenyew.petadoptsampleapp.ui.theme.PetAdoptTheme

@Composable
fun PetDetailsScreen(
    animalState: State<Animal?>,
    onItemFavorited: (isFavorited: Boolean) -> Unit,
    onBack: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    PetAdoptTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        null,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                onBack()
                            })
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = animalState.value?.name ?: "",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            },
            content = {
                PetDetailsContent(
                    animalState = animalState,
                    onItemFavorited = onItemFavorited,
                )
            }
        )
    }
}

@Preview
@Composable
fun PetDetailsScreen_Preview() {
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

    PetDetailsScreen(
        animalState = remember {
            mutableStateOf(animalModel)
        },
        onItemFavorited = {},
        onBack = { },
    )
}