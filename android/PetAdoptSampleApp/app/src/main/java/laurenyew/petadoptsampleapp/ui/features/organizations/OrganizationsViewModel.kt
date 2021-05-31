package laurenyew.petadoptsampleapp.ui.features.organizations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.database.organization.Organization
import laurenyew.petadoptsampleapp.repository.OrganizationSearchRepository
import laurenyew.petadoptsampleapp.repository.PetSearchRepository
import javax.inject.Inject

@HiltViewModel
class OrganizationsViewModel @Inject constructor(
    private val organizationsSearchRepository: OrganizationSearchRepository,
    private val petSearchRepository: PetSearchRepository,
) : ViewModel() {
    private var _lastSearchTerm = MutableStateFlow("")
    val lastSearchTerm: StateFlow<String> = _lastSearchTerm

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _organizations = MutableStateFlow<List<Organization>>(emptyList())
    val organizations: StateFlow<List<Organization>> = _organizations

    init {
        viewModelScope.launch {
            petSearchRepository.getLastSearchTerm()?.zipcode?.let {
                _lastSearchTerm.value = it
            }

            _isLoading.value = true
            _organizations.value =
                organizationsSearchRepository.fetchOrganizationListFromLastSearch()
            _isLoading.value = false
        }
    }
}