package laurenyew.petfindersampleapp.repository

import laurenyew.petfindersampleapp.database.favorite.FavoriteAnimal
import laurenyew.petfindersampleapp.database.favorite.FavoriteAnimalDatabaseProvider
import laurenyew.petfindersampleapp.repository.models.AnimalModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetFavoriteRepository @Inject constructor(
    private val favoriteAnimalDatabaseProvider: FavoriteAnimalDatabaseProvider
) {
    suspend fun favorites(): List<FavoriteAnimal> =
        favoriteAnimalDatabaseProvider.getAllFavoriteAnimals()

    suspend fun favoriteIds(): List<String> = favorites().map { it.id }

    suspend fun isFavorite(id: String): Boolean =
        favoriteAnimalDatabaseProvider.isAnimalFavorited(id)

    suspend fun favorite(animal: AnimalModel) {
        val favoriteAnimal = FavoriteAnimal(
            id = animal.id,
            name = animal.name,
            photoUrl = animal.photoUrl
        )
        favoriteAnimalDatabaseProvider.favoriteAnimal(favoriteAnimal)
    }

    suspend fun unFavorite(id: String) {
        favoriteAnimalDatabaseProvider.unFavoriteAnimal(id)
    }
}