package laurenyew.petadoptsampleapp.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * As per the official Android Developer's article
 * (https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda)
 * we should be making [collectAsState()] lifecycle aware
 * so that if the app is backgrounded and foregrounded that we don't
 * use resources unnecessarily
 */
@Composable
fun <T> Flow<T>.collectAsStateLifecycleAware(
    initial: T,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
): State<T> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val flowLifecycleAware: Flow<T> = remember(this, lifecycleOwner) {
        this.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    return flowLifecycleAware.collectAsState(initial, coroutineContext)
}