package laurenyew.petadoptsampleapp.database

import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimal
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimalDatabaseProvider
import javax.inject.Inject

class DatabaseManager @Inject constructor(
    private val database: PetAdoptDatabase
) : FavoriteAnimalDatabaseProvider {
    override suspend fun getAllFavoriteAnimals(): List<FavoriteAnimal> =
        database.favoriteAnimalDao().getFavoriteAnimals()

    override suspend fun getFavoriteAnimal(id: String): FavoriteAnimal? =
        database.favoriteAnimalDao().getFavoriteAnimal(id)

    override suspend fun isAnimalFavorited(id: String): Boolean =
        getFavoriteAnimal(id) != null

    override suspend fun favoriteAnimal(animal: FavoriteAnimal) {
        database.favoriteAnimalDao().insert(animal)
    }

    override suspend fun unFavoriteAnimal(id: String) {
        database.favoriteAnimalDao().deleteFavoriteAnimal(id)
    }
}