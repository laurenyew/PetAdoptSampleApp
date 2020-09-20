package laurenyew.petfindersampleapp.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import laurenyew.petfindersampleapp.di.keys.ViewModelKey
import laurenyew.petfindersampleapp.di.utils.ViewModelFactory
import laurenyew.petfindersampleapp.ui.favorites.FavoritesViewModel
import laurenyew.petfindersampleapp.ui.search.PetSearchViewModel

@Module
internal abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PetSearchViewModel::class)
    abstract fun bindPetSearchViewModel(petSearchViewModel: PetSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    abstract fun bindFavoritesViewModel(favoritesViewModel: FavoritesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}