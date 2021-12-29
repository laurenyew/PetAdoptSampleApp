package laurenyew.petadoptsampleapp.ui.features.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.data.PetFavoriteRepository
import laurenyew.petadoptsampleapp.data.PetSearchRepository
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.ui.features.petList.PetListViewModel
import javax.inject.Inject

@HiltViewModel
class PetSearchViewModel @Inject constructor(
    private val searchRepository: PetSearchRepository,
    favoriteRepository: PetFavoriteRepository,
) : PetListViewModel(favoriteRepository) {
    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location

    private val executeSearchFlow = MutableSharedFlow<Boolean>(0)

    val pagingDataFlow: Flow<PagingData<Animal>>

    init {
        // Load up list with the results of the last search
        viewModelScope.launch {
            searchRepository.getLastSearchTerm()?.let { searchTerm ->
                _location.value = searchTerm.zipcode
            }
        }

        pagingDataFlow = executeSearchFlow
            .flatMapLatest {
                searchRepository.getNearbyPets(location.value)
            }
            .cachedIn(viewModelScope)
    }

    fun setLocation(location: String) {
        _location.value = location
    }

    fun executeSearch() {
        val searchedLocation = location.value
        // Check location
        if (searchedLocation.isBlank()) {
            _errorState.value = "No location specified. Please specify a 5 digit zipcode."
            return
        } else if (searchedLocation.length != 5) {
            _errorState.value = "Invalid zipcode length. Please specify a 5 digit zipcode."
            return
        }
        viewModelScope.launch {
            executeSearchFlow.emit(true)
        }
    }
}