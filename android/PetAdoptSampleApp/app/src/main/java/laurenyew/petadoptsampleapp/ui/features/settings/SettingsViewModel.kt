package laurenyew.petadoptsampleapp.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.database.search.SearchTerm
import laurenyew.petadoptsampleapp.repository.PetSearchRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    searchRepository: PetSearchRepository
) : ViewModel() {
    private val _searchTermsList = MutableStateFlow<List<SearchTerm>>(emptyList())
    val searchTermsList: StateFlow<List<SearchTerm>> = _searchTermsList

    init {
        viewModelScope.launch {
            _searchTermsList.value = searchRepository.getSearchTerms()
        }
    }
}