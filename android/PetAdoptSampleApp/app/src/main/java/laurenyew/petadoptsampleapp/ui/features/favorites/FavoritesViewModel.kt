package laurenyew.petadoptsampleapp.ui.features.favorites

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.data.PetFavoriteRepository
import laurenyew.petadoptsampleapp.data.PetFavoriteRepository.Companion.DEFAULT_FAVORITES_FILTER
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.db.filter.AnimalFilter
import laurenyew.petadoptsampleapp.ui.features.petList.PetListViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteRepository: PetFavoriteRepository
) : PetListViewModel(favoriteRepository) {

    private val _filterState = MutableStateFlow(DEFAULT_FAVORITES_FILTER)
    val filterState: StateFlow<AnimalFilter> = _filterState

    private val _animals = MutableStateFlow<List<Animal>>(emptyList())

    val animals: StateFlow<List<Animal>>
        get() = _animals
            .combine(
                filterState,
                ::applyFilterState
            ).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = emptyList()
            )

    init {
        fetchFilterState()
        refreshFavorites()
    }


    fun refreshFavorites() {
        _isLoading.value = true
        viewModelScope.launch {
            val favorites = favoriteRepository.favorites().map {
                Animal(
                    animalId = it.animalId,
                    photoUrl = it.photoUrl,
                    name = it.name,
                    age = it.age,
                    sex = it.sex,
                    size = it.size,
                    species = it.species,
                    isFavorite = true,
                    index = it.index
                )
            }
            _isLoading.value = false
            _animals.value = favorites
        }
    }

    fun updateFilterState(filterState: AnimalFilter) {
        Timber.d("Update filter state: $filterState")
        viewModelScope.launch {
            favoriteRepository.saveFavoritesFilter(filterState)
            _filterState.value = filterState
            refreshFavorites()
        }
    }

    private fun fetchFilterState() {
        viewModelScope.launch {
            _filterState.value = favoriteRepository.getFavoritesFilter()
        }
    }

    private fun applyFilterState(
        animalList: List<Animal>,
        filter: AnimalFilter
    ): List<Animal> =
        animalList.filter { animal ->
            passesTypeFilterState(animal, filter)
                    && passesGenderFilterState(animal, filter)
        }

    private fun passesTypeFilterState(animal: Animal, filter: AnimalFilter): Boolean =
        (filter.showDogs && animal.isDog()) || (filter.showCats && animal.isCat())

    private fun passesGenderFilterState(animal: Animal, filter: AnimalFilter): Boolean =
        (filter.showFemales && animal.isFemale()) || (filter.showMales && animal.isMale())
}