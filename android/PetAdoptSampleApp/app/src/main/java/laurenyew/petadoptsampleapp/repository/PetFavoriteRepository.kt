package laurenyew.petadoptsampleapp.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import laurenyew.petadoptsampleapp.database.animal.Animal
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimal
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimalDatabaseProvider
import laurenyew.petadoptsampleapp.repository.networking.commands.PetDetailCommands
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetFavoriteRepository @Inject constructor(
    private val petDetailCommands: PetDetailCommands,
    private val favoriteAnimalDatabaseProvider: FavoriteAnimalDatabaseProvider,
    pollManager: PollManager,
    externalScope: CoroutineScope
) {
    init {
        // When data refresh flow is required, we need to refresh the favorites data
        pollManager.dataRefreshRequiredFlow.onEach {
            refreshFavoritesData()
        }.launchIn(externalScope)
    }

    suspend fun favorites(): List<FavoriteAnimal> =
        favoriteAnimalDatabaseProvider.getAllFavoriteAnimals()

    suspend fun favoriteIds(): List<String> = favorites().map { it.id }

    suspend fun isFavorite(id: String): Boolean =
        favoriteAnimalDatabaseProvider.isAnimalFavorited(id)

    suspend fun favorite(animal: Animal) {
        val favoriteAnimal = FavoriteAnimal(
            id = animal.animalId,
            name = animal.name,
            photoUrl = animal.photoUrl,
            age = animal.age,
            sex = animal.sex,
            size = animal.size
        )
        favoriteAnimalDatabaseProvider.favoriteAnimal(favoriteAnimal)
    }

    suspend fun unFavorite(id: String) {
        favoriteAnimalDatabaseProvider.unFavoriteAnimal(id)
    }

    private suspend fun refreshFavoritesData() {

    }
}