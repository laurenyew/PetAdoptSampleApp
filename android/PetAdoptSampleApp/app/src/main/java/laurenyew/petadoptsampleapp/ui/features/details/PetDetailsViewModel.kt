package laurenyew.petadoptsampleapp.ui.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.data.PetFavoriteRepository
import laurenyew.petadoptsampleapp.data.PetSearchRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PetDetailsViewModel @Inject constructor(
    private val searchRepository: PetSearchRepository,
    private val favoriteRepository: PetFavoriteRepository
) : ViewModel() {
    private val _animalState = MutableStateFlow<Animal?>(null)
    val animalState: StateFlow<Animal?> = _animalState

    fun setAnimalId(animalId: String) {
        viewModelScope.launch {
            refreshAnimalDetail(animalId)
        }
    }

    fun favorite() {
        animalState.value?.let { animal ->
            val id = animal.animalId
            Timber.d("Favorite: $id")
            viewModelScope.launch {
                favoriteRepository.favorite(animal)
                refreshAnimalDetail(id)
            }
        }
    }

    fun unfavorite() {
        animalState.value?.let { animal ->
            val id = animal.animalId
            Timber.d("Unfavorite: $id")
            viewModelScope.launch {
                favoriteRepository.unFavorite(id)
                refreshAnimalDetail(id)
            }
        }
    }

    private suspend fun refreshAnimalDetail(animalId: String) {
        val animalDetail = searchRepository.getAnimalDetails(animalId)
            ?.copy(favoriteRepository.isFavorite(animalId))
        _animalState.value = animalDetail
    }
}