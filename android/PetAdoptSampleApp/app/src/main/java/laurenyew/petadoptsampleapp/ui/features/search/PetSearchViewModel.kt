package laurenyew.petadoptsampleapp.ui.features.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.data.PetFavoriteRepository
import laurenyew.petadoptsampleapp.data.PetSearchRepository
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.ui.features.petList.PetListViewModel
import javax.inject.Inject

@HiltViewModel
class PetSearchViewModel @Inject constructor(
    private val searchRepository: PetSearchRepository,
    private val favoriteRepository: PetFavoriteRepository,
) : PetListViewModel(favoriteRepository) {
    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location

    private val searchPagingDataFlow: Flow<PagingData<Animal>>
    override val pagingDataFlow: Flow<PagingData<Animal>>
        get() = searchPagingDataFlow

    init {
        // Load up list with the results of the last search
        viewModelScope.launch {
            searchRepository.getLastSearchTerm()?.let { searchTerm ->
                _location.value = searchTerm.zipcode
            }
        }

        searchPagingDataFlow = location.flatMapLatest {
            searchRepository.getNearbyPets(it)
        }.cachedIn(viewModelScope)
    }

    fun setLocation(location: String) {
        _location.value = location
    }
}