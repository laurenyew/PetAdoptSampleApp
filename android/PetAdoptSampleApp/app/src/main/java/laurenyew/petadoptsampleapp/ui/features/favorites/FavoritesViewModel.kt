package laurenyew.petadoptsampleapp.ui.features.favorites

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.repository.PetFavoriteRepository
import laurenyew.petadoptsampleapp.repository.models.AnimalModel
import laurenyew.petadoptsampleapp.ui.features.list.PetListViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteRepository: PetFavoriteRepository
) : PetListViewModel(favoriteRepository) {

    init {
        refreshFavorites()
    }

    fun refreshFavorites() {
        _isLoading.value = true
        viewModelScope.launch {
            val favorites = favoriteRepository.favorites().map {
                AnimalModel(
                    id = it.id,
                    photoUrl = it.photoUrl,
                    name = it.name,
                    age = it.age,
                    sex = it.sex,
                    size = it.size,
                    isFavorite = true
                )
            }
            _isLoading.value = false
            _animals.value = favorites

        }
    }
}