package laurenyew.petfindersampleapp.di.modules

import dagger.Module
import dagger.Provides
import laurenyew.petfindersampleapp.repository.PetSearchRepository
import laurenyew.petfindersampleapp.repository.networking.commands.SearchPetsCommands
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun providePetSearchRepository(searchPetsCommands: SearchPetsCommands): PetSearchRepository =
        PetSearchRepository(searchPetsCommands)
}