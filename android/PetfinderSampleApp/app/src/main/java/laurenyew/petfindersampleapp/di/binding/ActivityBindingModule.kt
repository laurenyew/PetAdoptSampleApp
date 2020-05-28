package laurenyew.petfindersampleapp.di.binding

import dagger.Module
import dagger.android.ContributesAndroidInjector
import laurenyew.petfindersampleapp.MainActivity

@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    abstract fun bindMainActivity(): MainActivity?
}