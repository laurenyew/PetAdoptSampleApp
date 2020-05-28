package laurenyew.petfindersampleapp

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import laurenyew.petfindersampleapp.di.component.DaggerApplicationComponent

class BaseApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component = DaggerApplicationComponent.builder().application(this)?.build()
        component?.inject(this)
        return component!!
    }
}