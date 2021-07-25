package laurenyew.petadoptsampleapp.ui.features.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import laurenyew.petadoptsampleapp.R
import laurenyew.petadoptsampleapp.database.animal.Animal
import laurenyew.petadoptsampleapp.ui.common.FavoriteButton
import laurenyew.petadoptsampleapp.ui.common.LoadingImageView

@Composable
fun PetDetailsContent(
    animalState: State<Animal?>,
    onItemFavorited: (isFavorited: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val animal = animalState.value

    Column(modifier = modifier.fillMaxWidth()) {
        val unknown = stringResource(id = R.string.unknown)
        val age = animal?.age ?: unknown
        val sex = animal?.sex ?: unknown
        val size = animal?.size ?: unknown
        val description =
            stringResource(id = R.string.basic_info_formatted_string, age, sex, size)

        val imageModifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()

        LoadingImageView(
            imageUrl = animal?.photoUrl,
            modifier = imageModifier
        )

        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(4.dp)
        ) {
            FavoriteButton(
                isFavorite = animal?.isFavorite ?: false,
                onItemFavorited = onItemFavorited,
                modifier = Modifier
                    .size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(description, modifier = Modifier.align(Alignment.CenterVertically))
        }

        Spacer(modifier = Modifier.height(3.dp))
        Text(
            animal?.description ?: "A wonderful potential pet!",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(4.dp)
        )

        if (animal == null) {
            CircularProgressIndicator(
                modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun PetDetailsContent_Preview() {
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

    PetDetailsContent(
        animalState = remember {
            mutableStateOf(animalModel)
        }, onItemFavorited = {})
}