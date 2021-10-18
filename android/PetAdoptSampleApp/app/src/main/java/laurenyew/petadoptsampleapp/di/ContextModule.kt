package laurenyew.petadoptsampleapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.CancellationException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ContextModule {
    private var applicationCoroutineScope: CoroutineScope? = null

    // At the top level of your kotlin file:
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "petadopt_settings")

    @Provides
    fun providesContext(application: Application): Context = application.applicationContext


    @Provides
    @Singleton
    fun providesDataStore(context: Context): DataStore<Preferences> =
        context.dataStore

    /**
     * This application scope lives for the life of the application
     */
    @Provides
    fun providesApplicationCoroutineScope(): CoroutineScope {
        /**
         * If the scope hasn't been created or has been cancelled, create the scope
         */
        if (applicationCoroutineScope == null || applicationCoroutineScope?.isActive == false) {
            applicationCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
            Timber.d("Creating application scope")
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver {
            /**
             * On background, cancel application scope
             */
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                if (applicationCoroutineScope?.isActive == true) {
                    applicationCoroutineScope?.cancel(CancellationException("App Backgrounded"))
                    Timber.d("App is backgrounded. Cancelling application scope.")
                }
            }
        })
        return applicationCoroutineScope!!
    }

}