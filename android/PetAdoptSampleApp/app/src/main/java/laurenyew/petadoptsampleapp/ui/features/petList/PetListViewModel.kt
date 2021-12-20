package laurenyew.petadoptsampleapp.ui.features.petList

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.data.PetFavoriteRepository
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.ui.features.details.PetDetailsActivity
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
open class PetListViewModel @Inject constructor(
    private val favoriteRepository: PetFavoriteRepository
) : ViewModel() {
    open val pagingDataFlow: Flow<PagingData<Animal>> = flowOf(PagingData.empty())

    fun favorite(animal: Animal) {
        Timber.d("Favorite: ${animal.animalId}")
        viewModelScope.launch {
            favoriteRepository.favorite(animal)
        }
    }

    fun unfavorite(animal: Animal) {
        Timber.d("Unfavorite: ${animal.animalId}")
        viewModelScope.launch {
            favoriteRepository.unFavorite(animal)
        }
    }

    fun openAnimalDetail(context: Context, id: String) {
        Timber.d("Open Animal Detail $id")
        val intent = Intent(context, PetDetailsActivity::class.java)
        intent.putExtra(PetDetailsActivity.ANIMAL_ID_KEY, id)
        context.startActivity(intent)
    }
}