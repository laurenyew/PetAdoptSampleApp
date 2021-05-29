package laurenyew.petadoptsampleapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ContextModule {
    @Provides
    fun providesContext(application: Application): Context = application.applicationContext

    @Provides
    fun providesSharedPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences("petadoptsharedprefs", Context.MODE_PRIVATE)
}