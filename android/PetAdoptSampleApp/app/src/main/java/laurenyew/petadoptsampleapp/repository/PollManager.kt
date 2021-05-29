package laurenyew.petadoptsampleapp.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * PetFinder API terms of service requires that we update our data at least once every hour.
 * Subscribe to [dataRefreshRequiredFlow] to be notified of when to update
 */
class PollManager @Inject constructor(
    private val externalScope: CoroutineScope,
    private val pollIntervalMillis: Long = DEFAULT_POLL_INTERVAL_MILLIS
) {
    private val _pollFlow = MutableSharedFlow<Unit>(replay = 0) // only update once, no replay
    val dataRefreshRequiredFlow: SharedFlow<Unit> = _pollFlow

    init {
        externalScope.launch {
            while (true) {
                _pollFlow.emit(Unit)
                delay(pollIntervalMillis)
            }
        }
    }

    companion object {
        private const val DEFAULT_POLL_INTERVAL_MILLIS: Long = 1000 * 60 * 60 // poll every hour
    }
}