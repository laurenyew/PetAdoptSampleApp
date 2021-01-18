package laurenyew.petfindersampleapp.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import laurenyew.petfindersampleapp.repository.networking.api.PetfinderApi
import laurenyew.petfindersampleapp.repository.networking.api.PetfinderApiConstants
import laurenyew.petfindersampleapp.repository.networking.api.PetfinderTokenApi
import laurenyew.petfindersampleapp.repository.networking.auth.AccessTokenProvider
import laurenyew.petfindersampleapp.repository.networking.auth.PetfinderAccessTokenAuthenticator
import laurenyew.petfindersampleapp.repository.networking.auth.PetfinderTokenRepository
import laurenyew.petfindersampleapp.repository.networking.commands.AuthCommands
import laurenyew.petfindersampleapp.repository.networking.commands.SearchPetsCommands
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providePetfinderTokenApi(): PetfinderTokenApi {
        //Setup HttpClient
        val okHttpClient = OkHttpClient.Builder().build()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(PetfinderApiConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient).build()
        return retrofit.create(PetfinderTokenApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAccessTokenProvider(
        authCommands: AuthCommands,
        @ApplicationContext context: Context?
    ): AccessTokenProvider =
        PetfinderTokenRepository(authCommands, context)

    @Singleton
    @Provides
    fun providePetfinderApi(accessTokenProvider: AccessTokenProvider): PetfinderApi {
        //Setup HttpClient
        val okHttpClient = OkHttpClient.Builder().apply {
            // Add headers
            addInterceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("Accept-Language", Locale.getDefault().language)
                    .build()
                it.proceed(request)
            }
            authenticator(PetfinderAccessTokenAuthenticator(accessTokenProvider))
        }.build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(PetfinderApiConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient).build()

        return retrofit.create(PetfinderApi::class.java)
    }


    @Provides
    fun provideSearchPetsCommands(api: PetfinderApi): SearchPetsCommands = SearchPetsCommands(api)

    @Provides
    fun provideAuthCommands(api: PetfinderTokenApi): AuthCommands = AuthCommands(api)
}