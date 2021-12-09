package laurenyew.petadoptsampleapp.ui.features.search

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.data.PetFavoriteRepository
import laurenyew.petadoptsampleapp.data.PetSearchRepository
import laurenyew.petadoptsampleapp.data.responses.SearchPetsRepoResponse
import laurenyew.petadoptsampleapp.ui.features.petList.PetListViewModel
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class PetSearchViewModel @Inject constructor(
    private val searchRepository: PetSearchRepository,
    private val favoriteRepository: PetFavoriteRepository,
) : PetListViewModel(favoriteRepository) {
    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location

    private var currentSearchLocation = ""
    private var currentSearchJob: Job? = null

    init {
        // Load up list with the results of the last search
        viewModelScope.launch {
            searchRepository.getLastSearchTerm()?.let { searchTerm ->
                _location.value = searchTerm.zipcode
                currentSearchLocation = _location.value
                val savedAnimalList = searchRepository.getSearchedAnimalList(searchTerm.searchId)
                val updatedSavedAnimalListWithFavorites = parseWithFavorites(savedAnimalList)
                if (updatedSavedAnimalListWithFavorites != null) {
                    _animals.value = updatedSavedAnimalListWithFavorites // use saved list
                } else {
                    searchAnimals() // restart search with last search term
                }
            }
        }
    }

    fun setLocation(location: String) {
        _location.value = location
    }

    fun searchAnimals(forceRefresh: Boolean = false) {
        val newLocation = location.value

        // Check location
        if (newLocation.isBlank()) {
            _errorState.value = "No location specified. Please specify a 5 digit zipcode."
            return
        } else if (newLocation.length != 5) {
            _errorState.value = "Invalid zipcode length. Please specify a 5 digit zipcode."
            return
        }

        val isChangedLocation = newLocation != currentSearchLocation
        if (isChangedLocation || forceRefresh) {
            if (currentSearchJob?.isActive == true) {
                currentSearchJob?.cancel(CancellationException("New search started. Cancelling old search."))
            }

            _isLoading.value = true
            if (isChangedLocation) {
                _animals.value = emptyList()
            }

            currentSearchLocation = newLocation
            currentSearchJob = viewModelScope.launch {
                when (val searchResponse = searchRepository.getNearbyPets(newLocation)) {
                    is SearchPetsRepoResponse.Success -> {
                        val searchedAnimals = parseWithFavorites(searchResponse.animals)
                        _animals.value = searchedAnimals ?: emptyList()
                        _isError.value = false

                        searchRepository.saveSearchTerm(newLocation)
                    }
                    else -> {
                        _animals.value = emptyList()
                        _isError.value = true
                    }
                }
                _isLoading.value = false
            }
        }
    }

    private suspend fun parseWithFavorites(animals: List<Animal>?): List<Animal>? {
        val favorites =
            favoriteRepository.favoriteIds()
        return animals?.map {
            if (favorites.contains(it.animalId)) {
                it.copy(true)
            } else {
                it
            }
        }
    }

}