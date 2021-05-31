package laurenyew.petadoptsampleapp.ui.features.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.repository.PetFavoriteRepository
import laurenyew.petadoptsampleapp.database.animal.Animal
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
open class PetListViewModel @Inject constructor(
    private val favoriteRepository: PetFavoriteRepository
) : ViewModel() {
    protected val _animals = MutableStateFlow<List<Animal>>(emptyList())
    protected val _isLoading = MutableStateFlow(false)
    protected val _isError = MutableStateFlow(false)
    open val animals: StateFlow<List<Animal>> = _animals
    val isLoading: StateFlow<Boolean> = _isLoading
    val isError: StateFlow<Boolean> = _isError

    fun favorite(animal: Animal) {
        Timber.d("Favorite: ${animal.animalId}")
        viewModelScope.launch {
            favoriteRepository.favorite(animal)

            val favoriteId = animal.animalId
            updateAnimalsForFavorite(favoriteId, true)
        }
    }

    fun unfavorite(id: String) {
        Timber.d("Unfavorite: $id")
        viewModelScope.launch {
            favoriteRepository.unFavorite(id)

            updateAnimalsForFavorite(id, false)
        }
    }

    fun openAnimalDetail(id: String) {
        Timber.d("Open Animal Detail $id")
    }

    private fun updateAnimalsForFavorite(id: String, favorite: Boolean) {
        val updatedAnimals = _animals.value.map {
            if (it.animalId == id) {
                it.copy(favorite)
            } else {
                it
            }
        }
        _animals.value = updatedAnimals
    }
}