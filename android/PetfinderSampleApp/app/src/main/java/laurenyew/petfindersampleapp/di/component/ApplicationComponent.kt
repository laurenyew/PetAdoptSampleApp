package laurenyew.petfindersampleapp.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import laurenyew.petfindersampleapp.BaseApplication
import laurenyew.petfindersampleapp.di.binding.ActivityBindingModule
import laurenyew.petfindersampleapp.di.binding.FragmentBindingModule
import laurenyew.petfindersampleapp.di.modules.ContextModule
import laurenyew.petfindersampleapp.di.modules.NetworkModule
import laurenyew.petfindersampleapp.di.modules.RepositoryModule
import laurenyew.petfindersampleapp.di.modules.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        ActivityBindingModule::class,
        FragmentBindingModule::class,
        ContextModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelModule::class]
)
interface ApplicationComponent : AndroidInjector<DaggerApplication> {
    fun inject(application: BaseApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application?): Builder?
        fun build(): ApplicationComponent?
    }
}