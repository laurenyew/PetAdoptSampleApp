package laurenyew.petadoptsampleapp.ui.features.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import laurenyew.petadoptsampleapp.repository.PetSearchRepository
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    searchRepository: PetSearchRepository
): ViewModel() {
    val lastSearchTerm = searchRepository.getLastSearchTerm()

}