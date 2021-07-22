package laurenyew.petadoptsampleapp.ui.features.petList

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.database.animal.Animal
import laurenyew.petadoptsampleapp.repository.PetFavoriteRepository
import laurenyew.petadoptsampleapp.ui.features.details.PetDetailsActivity
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
open class PetListViewModel @Inject constructor(
    private val favoriteRepository: PetFavoriteRepository
) : ViewModel() {
    protected val _animals = MutableStateFlow<List<Animal>>(emptyList())
    protected val _isLoading = MutableStateFlow(false)
    protected val _isError = MutableStateFlow(false)
    protected val _errorState = MutableStateFlow<String?>(null)
    open val animals: StateFlow<List<Animal>> = _animals
    val isLoading: StateFlow<Boolean> = _isLoading
    val isError: StateFlow<Boolean> = _isError
    val errorState: StateFlow<String?> = _errorState

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

    fun openAnimalDetail(context: Context, id: String) {
        Timber.d("Open Animal Detail $id")
        val intent = Intent(context, PetDetailsActivity::class.java)
        intent.putExtra(PetDetailsActivity.ANIMAL_ID_KEY, id)
        context.startActivity(intent)
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