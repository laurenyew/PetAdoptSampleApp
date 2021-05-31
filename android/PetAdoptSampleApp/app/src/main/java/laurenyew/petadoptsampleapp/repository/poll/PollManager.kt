package laurenyew.petadoptsampleapp.repository.poll

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import javax.inject.Inject

/**
 * PetFinder API terms of service requires that we update our data at least once every hour.
 * Subscribe to [dataRefreshRequiredFlow] to be notified of when to update
 */
class PollManager @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val externalScope: CoroutineScope,
    private val pollIntervalMillis: Long = DEFAULT_POLL_INTERVAL_MILLIS,
) : PollUpdateAPI {
    private val _pollFlow = MutableSharedFlow<Long>(replay = 0) // only update once, no replay
    val dataRefreshRequiredFlow: SharedFlow<Long> = _pollFlow

    private var pollJob: Job? = null
    private var numPolls = 0

    private var listener: PollUpdateAPI.PollUpdateListener? = null

    init {
        restartPoll()
    }

    fun lastPollTime(): Long = sharedPreferences.getLong(LAST_POLL_KEY, -1L)


    fun isPastInterval(
        newPollTime: Long,
        oldPollTime: Long,
        pollInterval: Long = pollIntervalMillis
    ): Boolean {
        val diffPollTimes = newPollTime - oldPollTime
        return diffPollTimes > pollInterval
    }

    private fun isPastLastPollTime(currentTime: Long, pollInterval: Long): Boolean {
        val lastPollTime = lastPollTime()
        return lastPollTime == -1L || isPastInterval(currentTime, lastPollTime, pollInterval)
    }

    private fun diffLastPollTime(): Long = System.currentTimeMillis() - lastPollTime()

    private fun restartPoll(pollInterval: Long = pollIntervalMillis) {
        pollJob?.cancel(CancellationException("Restarting Poll on interval: $pollInterval millis"))
        pollJob = externalScope.launch {
            while (true) {
                val currentTime = System.currentTimeMillis()
                if (isPastLastPollTime(currentTime, pollInterval)) {
                    _pollFlow.emit(currentTime)
                    onPoll()
                    delay(pollInterval)
                } else {
                    delay(pollInterval - diffLastPollTime())
                }
            }
        }
    }

    private fun onPoll() {
        sharedPreferences.edit()
            .putLong(LAST_POLL_KEY, System.currentTimeMillis())
            .apply()
        numPolls++
        listener?.onPollCompleted(pollNum = numPolls)
    }

    override fun updatePollIntervalTime(newPollIntervalTime: Long?) {
        restartPoll(newPollIntervalTime ?: pollIntervalMillis)
    }

    override fun listenForPollUpdates(
        callbackUpdateListener: PollUpdateAPI.PollUpdateListener
    ) {
        listener = callbackUpdateListener
    }


    companion object {
        private const val DEFAULT_POLL_INTERVAL_MILLIS: Long = 1000 * 60 * 60 // poll every hour
        private const val LAST_POLL_KEY = "last_refresh_poll"
    }
}