package laurenyew.petadoptsampleapp.repository

import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimal
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimalDatabaseProvider
import laurenyew.petadoptsampleapp.database.animal.Animal
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
}