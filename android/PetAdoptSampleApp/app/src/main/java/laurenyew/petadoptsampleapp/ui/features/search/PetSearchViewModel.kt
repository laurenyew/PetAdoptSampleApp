package laurenyew.petadoptsampleapp.ui.features.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.repository.PetFavoriteRepository
import laurenyew.petadoptsampleapp.repository.PetSearchRepository
import laurenyew.petadoptsampleapp.repository.responses.SearchPetsRepoResponse
import laurenyew.petadoptsampleapp.ui.features.petList.PetListViewModel
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class PetSearchViewModel @Inject constructor(
    private val searchRepository: PetSearchRepository,
    private val favoriteRepository: PetFavoriteRepository,
) : PetListViewModel(favoriteRepository) {
    val location: MutableState<String> =
        mutableStateOf("")

    private var currentSearchLocation = ""
    private var currentSearchJob: Job? = null

    init {
        // Load up list with the results of the last search
        viewModelScope.launch {
            searchRepository.getLastSearchTerm()?.let { searchTerm ->
                location.value = searchTerm.zipcode
                val savedAnimalList = searchRepository.getSearchedAnimalList(searchTerm.searchId)
                if (savedAnimalList != null) {
                    _animals.value = savedAnimalList // use saved list
                } else {
                    searchAnimals() // restart search with last search term
                }
            }
        }
    }

    fun searchAnimals() {
        val newLocation = location.value
        if (newLocation != currentSearchLocation
            && newLocation.isNotBlank()
            && newLocation.length >= 5
        ) {
            if (currentSearchJob?.isActive == true) {
                currentSearchJob?.cancel(CancellationException("New search started. Cancelling old search."))
            }

            _isLoading.value = true
            currentSearchLocation = newLocation
            currentSearchJob = viewModelScope.launch {
                when (val searchResponse = searchRepository.getNearbyPets(newLocation)) {
                    is SearchPetsRepoResponse.Success -> {
                        val favorites =
                            favoriteRepository.favoriteIds()
                        val searchedAnimals =
                            searchResponse.animals?.map {
                                if (favorites.contains(it.animalId)) {
                                    it.copy(true)
                                } else {
                                    it
                                }
                            }

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
}