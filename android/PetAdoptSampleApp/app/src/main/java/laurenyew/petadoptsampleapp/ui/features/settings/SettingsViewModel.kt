package laurenyew.petadoptsampleapp.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import laurenyew.petadoptsampleapp.database.search.SearchTerm
import laurenyew.petadoptsampleapp.repository.PetSearchRepository
import laurenyew.petadoptsampleapp.repository.poll.PollManager
import laurenyew.petadoptsampleapp.repository.poll.PollUpdateAPI
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    searchRepository: PetSearchRepository,
    private val pollManager: PollManager
) : ViewModel() {
    private val _searchTermsList = MutableStateFlow<List<SearchTerm>>(emptyList())
    val searchTermsList: StateFlow<List<SearchTerm>> = _searchTermsList

    private val _lastPollTime = MutableStateFlow("")
    val lastPollTime: StateFlow<String> = _lastPollTime

    val pollNumStateFlow = listenForPollUpdates().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 0
    )

    private val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.US)


    init {
        viewModelScope.launch {
            _searchTermsList.value = searchRepository.getSearchTerms()
            _lastPollTime.value = parsePollTime(pollManager.lastPollTime())
        }
    }

    fun updatePollIntervalTime(newIntervalTime: Long? = null) {
        pollManager.updatePollIntervalTime(newIntervalTime)
    }

    private fun listenForPollUpdates(): Flow<Int> = callbackFlow {
        val job = viewModelScope.launch {
            pollManager.listenForPollUpdates(object : PollUpdateAPI.PollUpdateListener {
                override fun onPollCompleted(pollNum: Int) {
                    offer(pollNum)
                }
            })
        }
        awaitClose {
            job.cancel()
        }
    }

    private fun parsePollTime(pollTime: Long): String =
        if (pollTime == -1L) {
            ""
        } else {
            dateFormat.format(Date(pollTime))
        }
}