package laurenyew.petadoptsampleapp.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.data.PetSearchRepository
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    searchRepository: PetSearchRepository
) : ViewModel() {
    private val _lastSearchTerm = MutableStateFlow("")
    val lastSearchTerm: StateFlow<String> = _lastSearchTerm

    init {
        viewModelScope.launch {
            searchRepository.getLastSearchTerm()?.let {
                _lastSearchTerm.value = it.zipcode
            }
        }
    }
}