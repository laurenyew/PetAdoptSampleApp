package laurenyew.petfindersampleapp.di

import dagger.Component
import laurenyew.petfindersampleapp.MainActivity

@Component(modules = [NetworkModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
}