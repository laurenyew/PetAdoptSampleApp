package laurenyew.petfindersampleapp.ui.features.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import laurenyew.petfindersampleapp.repository.PetFavoriteRepository
import laurenyew.petfindersampleapp.repository.PetSearchRepository
import laurenyew.petfindersampleapp.repository.models.AnimalModel
import laurenyew.petfindersampleapp.repository.responses.SearchPetsRepoResponse
import javax.inject.Inject

@HiltViewModel
class PetSearchViewModel @Inject constructor(
    private val searchRepository: PetSearchRepository,
    private val favoriteRepository: PetFavoriteRepository
) : ViewModel() {
    private val _animals = MutableStateFlow<List<AnimalModel>>(emptyList())

    private val _isLoading = MutableStateFlow(false)
    private val _isError = MutableStateFlow(false)
    val animals: StateFlow<List<AnimalModel>> = _animals
    val isLoading: StateFlow<Boolean> = _isLoading
    val isError: StateFlow<Boolean> = _isError

    val location: MutableState<String> = mutableStateOf("")

    fun searchAnimals() {
        val newLocation = location.value
        if (newLocation.isNotBlank() && newLocation.length >= 5) {
            _isLoading.value = true

            viewModelScope.launch {
                val searchResponse = searchRepository.getNearbyDogs(newLocation)
                when (searchResponse) {
                    is SearchPetsRepoResponse.Success -> {
                        val favorites =
                            favoriteRepository.favoriteIds()
                        val searchedAnimals =
                            searchResponse.animals?.map {
                                it.apply {
                                    it.isFavorite = favorites.contains(it.id)
                                }
                            }

                        _animals.value = searchedAnimals ?: emptyList()
                        _isError.value = false
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

    fun favorite(animalModel: AnimalModel) {
        viewModelScope.launch {
            favoriteRepository.favorite(animalModel)

            val favoriteId = animalModel.id
            val updatedAnimals = _animals.value.map {
                it.apply {
                    if (id == favoriteId) {
                        it.isFavorite = true
                    }
                }
            }

            _animals.value = updatedAnimals
        }
    }

    fun unfavorite(id: String) {
        viewModelScope.launch {
            favoriteRepository.unFavorite(id)

            val unfavoriteId = id
            val updatedAnimals = _animals.value.map {
                it.apply {
                    if (id == unfavoriteId) {
                        it.isFavorite = false
                    }
                }
            }

            _animals.value = updatedAnimals
        }
    }

    fun openAnimalDetail(id: String) {
        //TODO
    }

}