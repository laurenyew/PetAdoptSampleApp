package laurenyew.petadoptsampleapp.ui.features.details

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import laurenyew.petadoptsampleapp.utils.collectAsStateLifecycleAware

@AndroidEntryPoint
class PetDetailsActivity : AppCompatActivity() {
    private val detailsViewModel: PetDetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animalId = intent.getStringExtra(ANIMAL_ID_KEY)
            ?: throw RuntimeException("Invalid Pet Detail! No Animal ID specified.")
        detailsViewModel.setAnimalId(animalId)
        setContent {
            val animal = detailsViewModel.animalState.collectAsStateLifecycleAware(initial = null)
            PetDetailsScreen(
                animalState = animal,
                onItemFavorited = { isFavorited ->
                    if (isFavorited) {
                        detailsViewModel.favorite()
                    } else {
                        detailsViewModel.unfavorite()
                    }
                },
                onBack = {
                    onBackPressed()
                })
        }
    }

    companion object {
        const val ANIMAL_ID_KEY = "animal_id"
    }
}