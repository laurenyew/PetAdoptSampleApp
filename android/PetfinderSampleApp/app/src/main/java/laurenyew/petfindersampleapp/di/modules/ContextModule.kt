package laurenyew.petfindersampleapp.di.modules

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Module
abstract class ContextModule {
    @Binds
    abstract fun provideContext(application: Application?): Context?
}