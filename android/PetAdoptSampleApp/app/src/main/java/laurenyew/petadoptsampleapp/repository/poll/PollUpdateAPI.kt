package laurenyew.petadoptsampleapp.repository.poll

interface PollUpdateAPI {
    interface PollUpdateListener {
        fun onPollCompleted(pollNum: Int)
    }

    fun updatePollIntervalTime(newPollIntervalTime: Long?)

    fun listenForPollUpdates(
        callbackUpdateListener: PollUpdateListener
    )
}