package laurenyew.petadoptsampleapp.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.db.favorite.FavoriteAnimalDatabaseProvider
import laurenyew.petadoptsampleapp.db.filter.AnimalFilter
import laurenyew.petadoptsampleapp.db.filter.AnimalFilterDatabaseProvider
import laurenyew.petadoptsampleapp.data.networking.commands.PetDetailCommands
import laurenyew.petadoptsampleapp.data.poll.PollManager
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetFavoriteRepository @Inject constructor(
    private val petDetailCommands: PetDetailCommands,
    private val favoriteAnimalDatabaseProvider: FavoriteAnimalDatabaseProvider,
    private val animalFilterDatabaseProvider: AnimalFilterDatabaseProvider,
    pollManager: PollManager,
    externalScope: CoroutineScope
) {
    init {
        // When data refresh flow is required, we need to refresh the favorites data
        pollManager.dataRefreshRequiredFlow.onEach {
            refreshFavoritesData()
        }.launchIn(externalScope)
    }

    suspend fun favorites(): List<Animal> =
        favoriteAnimalDatabaseProvider.getAllFavoriteAnimals()

    suspend fun favoriteIds(): List<Long> = favorites().map { it.animalId }

    suspend fun isFavorite(id: String): Boolean =
        favoriteAnimalDatabaseProvider.isAnimalFavorited(id)

    suspend fun favorite(animal: Animal) {
        favoriteAnimalDatabaseProvider.favoriteAnimal(animal)
    }

    suspend fun unFavorite(animal: Animal) {
        favoriteAnimalDatabaseProvider.unFavoriteAnimal(animal)
    }

    suspend fun getFavoritesFilter(): AnimalFilter =
        animalFilterDatabaseProvider.animalFilter() ?: DEFAULT_FAVORITES_FILTER

    suspend fun saveFavoritesFilter(favoritesFilter: AnimalFilter) {
        animalFilterDatabaseProvider.updateAnimalFilter(favoritesFilter)
    }

    private suspend fun refreshFavoritesData() {
        favorites().forEach {
            val updatedAnimal = petDetailCommands.fetchAnimalDetails(it.animalId)
            if (updatedAnimal != null) {
                favoriteAnimalDatabaseProvider.updateAnimal(updatedAnimal)
            } else {
                Timber.e("Failed to get updated details for favorite animal. Data may be out of date")
            }
        }
    }


    companion object {
        val DEFAULT_FAVORITES_FILTER = AnimalFilter()
    }
}