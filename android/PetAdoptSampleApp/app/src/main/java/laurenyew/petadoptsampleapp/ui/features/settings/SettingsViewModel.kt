package laurenyew.petadoptsampleapp.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.database.search.SearchTerm
import laurenyew.petadoptsampleapp.repository.PetSearchRepository
import laurenyew.petadoptsampleapp.repository.PollManager
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    searchRepository: PetSearchRepository,
    pollManager: PollManager
) : ViewModel() {
    private val _searchTermsList = MutableStateFlow<List<SearchTerm>>(emptyList())
    val searchTermsList: StateFlow<List<SearchTerm>> = _searchTermsList

    private val _lastPollTime = MutableStateFlow("")
    val lastPollTime: StateFlow<String> = _lastPollTime

    private val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.US)

    init {
        viewModelScope.launch {
            _searchTermsList.value = searchRepository.getSearchTerms()
            _lastPollTime.value = parsePollTime(pollManager.lastPollTime())
        }
    }

    private fun parsePollTime(pollTime: Long): String =
        if (pollTime == -1L) {
            ""
        } else {
            dateFormat.format(Date(pollTime))
        }
}