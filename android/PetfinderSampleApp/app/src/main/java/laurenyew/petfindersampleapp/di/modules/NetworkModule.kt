package laurenyew.petfindersampleapp.di.modules

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import laurenyew.petfindersampleapp.repository.networking.api.PetfinderApi
import laurenyew.petfindersampleapp.repository.networking.api.PetfinderApiConstants
import laurenyew.petfindersampleapp.repository.networking.commands.SearchPetsCommands
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
class NetworkModule {
    private val authToken = "" //TODO: Fill in with your auth token

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {
        //Setup HttpClient
        val httpClientBuilder = OkHttpClient.Builder()

        //Add headers
        httpClientBuilder.addInterceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("Accept-Language", Locale.getDefault().language)
                .addHeader("Authorization", "Bearer $authToken")
                .build()
            it.proceed(request)
        }
        return httpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder()
            .baseUrl(PetfinderApiConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient).build()
    }

    @Singleton
    @Provides
    fun providePetfinderApi(retrofit: Retrofit): PetfinderApi =
        retrofit.create(PetfinderApi::class.java)

    @Provides
    fun provideSearchPetsCommands(api: PetfinderApi): SearchPetsCommands = SearchPetsCommands(api)
}