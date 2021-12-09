package laurenyew.petadoptsampleapp.db.util

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import laurenyew.petadoptsampleapp.db.animal.Animal

class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val animalListType = Types.newParameterizedType(List::class.java, Animal::class.java)
    private val animalAdapter = moshi.adapter<List<Animal>>(animalListType)

    @TypeConverter
    fun animalListToJson(value: List<Animal>?): String =
        animalAdapter.toJson(value)


    @TypeConverter
    fun jsonToAnimalList(value: String): List<Animal>? =
        animalAdapter.fromJson(value)
}