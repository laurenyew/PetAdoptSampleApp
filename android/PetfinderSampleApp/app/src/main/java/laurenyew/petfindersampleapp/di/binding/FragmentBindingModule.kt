package laurenyew.petfindersampleapp.di.binding

import dagger.Module
import dagger.android.ContributesAndroidInjector
import laurenyew.petfindersampleapp.ui.favorites.FavoritesFragment
import laurenyew.petfindersampleapp.ui.search.PetSearchFragment

@Module
abstract class FragmentBindingModule {
    @ContributesAndroidInjector
    abstract fun bindPetSearchFragment(): PetSearchFragment

    @ContributesAndroidInjector
    abstract fun bindFavoritesFragment(): FavoritesFragment
}