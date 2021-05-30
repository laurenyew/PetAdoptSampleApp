package laurenyew.petadoptsampleapp.repository

import android.content.SharedPreferences
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
    private val sharedPreferences: SharedPreferences,
    externalScope: CoroutineScope,
    private val pollIntervalMillis: Long = DEFAULT_POLL_INTERVAL_MILLIS,
) {
    private val _pollFlow = MutableSharedFlow<Unit>(replay = 0) // only update once, no replay
    val dataRefreshRequiredFlow: SharedFlow<Unit> = _pollFlow

    init {
        externalScope.launch {
            while (true) {
                if (isPastLastPollTime()) {
                    _pollFlow.emit(Unit)
                    onPoll()
                }
                delay(pollIntervalMillis)
            }
        }
    }

    fun lastPollTime(): Long = sharedPreferences.getLong(LAST_POLL_KEY, -1L)

    private fun isPastLastPollTime(): Boolean {
        val lastPollTime = lastPollTime()
        val diffBetweenLastPollTimeAndNow = System.currentTimeMillis() - lastPollTime
        return lastPollTime == -1L || (diffBetweenLastPollTimeAndNow > pollIntervalMillis)
    }

    private fun onPoll() {
        sharedPreferences.edit()
            .putLong(LAST_POLL_KEY, System.currentTimeMillis())
            .apply()
    }

    companion object {
        private const val DEFAULT_POLL_INTERVAL_MILLIS: Long = 1000 * 60 * 60 // poll every hour
        private const val LAST_POLL_KEY = "last_refresh_poll"
    }
}