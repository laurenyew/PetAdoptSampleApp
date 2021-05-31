package laurenyew.petadoptsampleapp.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import laurenyew.petadoptsampleapp.database.animal.Animal
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimal
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimalDatabaseProvider
import laurenyew.petadoptsampleapp.database.favorite.FavoritesFilterDatabaseProvider
import laurenyew.petadoptsampleapp.repository.networking.commands.PetDetailCommands
import laurenyew.petadoptsampleapp.ui.features.favorites.FavoritesFilter
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetFavoriteRepository @Inject constructor(
    private val petDetailCommands: PetDetailCommands,
    private val favoriteAnimalDatabaseProvider: FavoriteAnimalDatabaseProvider,
    private val favoritesFilterDatabaseProvider: FavoritesFilterDatabaseProvider,
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
            size = animal.size,
            type = animal.type
        )
        favoriteAnimalDatabaseProvider.favoriteAnimal(favoriteAnimal)
    }

    suspend fun unFavorite(id: String) {
        favoriteAnimalDatabaseProvider.unFavoriteAnimal(id)
    }

    suspend fun getFavoritesFilter(): FavoritesFilter =
        favoritesFilterDatabaseProvider.getFavoritesFilter() ?: DEFAULT_FAVORITES_FILTER

    suspend fun saveFavoritesFilter(favoritesFilter: FavoritesFilter) {
        favoritesFilterDatabaseProvider.updateFavoritesFilter(favoritesFilter)
    }

    private suspend fun refreshFavoritesData() {
        favorites().forEach {
            val updatedAnimal = petDetailCommands.fetchAnimalDetails(it.id)
            if (updatedAnimal != null) {
                val updatedFavoritedAnimal = FavoriteAnimal(
                    id = it.id,
                    name = updatedAnimal.name,
                    photoUrl = updatedAnimal.photoUrl,
                    age = updatedAnimal.age,
                    sex = updatedAnimal.sex,
                    size = updatedAnimal.size,
                    type = updatedAnimal.type
                )
                favoriteAnimalDatabaseProvider.updateFavoritedAnimal(updatedFavoritedAnimal)
            } else {
                Timber.e("Failed to get updated details for favorite animal. Data may be out of date")
            }
        }
    }


    companion object {
        val DEFAULT_FAVORITES_FILTER = FavoritesFilter()
    }
}