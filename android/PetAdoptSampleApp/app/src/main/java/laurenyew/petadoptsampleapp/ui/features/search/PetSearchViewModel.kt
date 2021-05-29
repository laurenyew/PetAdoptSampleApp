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
import laurenyew.petadoptsampleapp.ui.features.list.PetListViewModel
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class PetSearchViewModel @Inject constructor(
    private val searchRepository: PetSearchRepository,
    private val favoriteRepository: PetFavoriteRepository,
) : PetListViewModel(favoriteRepository) {
    val location: MutableState<String> =
        mutableStateOf(searchRepository.getLastSearchTerm())

    private var currentSearchLocation = ""
    private var currentSearchJob: Job? = null

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
                when (val searchResponse = searchRepository.getNearbyDogs(newLocation)) {
                    is SearchPetsRepoResponse.Success -> {
                        val favorites =
                            favoriteRepository.favoriteIds()
                        val searchedAnimals =
                            searchResponse.animals?.map {
                                if (favorites.contains(it.id)) {
                                    it.copy(true)
                                } else {
                                    it
                                }
                            }

                        _animals.value = searchedAnimals ?: emptyList()
                        _isError.value = false

                        searchRepository.saveSearchTerms(newLocation)
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