package laurenyew.petadoptsampleapp.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApi
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptTokenApi
import laurenyew.petadoptsampleapp.repository.networking.auth.AccessTokenProvider
import laurenyew.petadoptsampleapp.repository.networking.auth.PetAdoptAccessTokenAuthenticator
import laurenyew.petadoptsampleapp.repository.networking.auth.PetAdoptTokenRepository
import laurenyew.petadoptsampleapp.repository.networking.commands.AuthCommands
import laurenyew.petadoptsampleapp.repository.networking.commands.SearchPetsCommands
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providePetAdoptTokenApi(): PetAdoptTokenApi {
        //Setup HttpClient
        val okHttpClient = OkHttpClient.Builder().build()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(PetAdoptApiConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient).build()
        return retrofit.create(PetAdoptTokenApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAccessTokenProvider(
        authCommands: AuthCommands,
        @ApplicationContext context: Context?
    ): AccessTokenProvider =
        PetAdoptTokenRepository(authCommands, context)

    @Singleton
    @Provides
    fun providePetAdoptApi(accessTokenProvider: AccessTokenProvider): PetAdoptApi {
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
            authenticator(PetAdoptAccessTokenAuthenticator(accessTokenProvider))
        }.build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(PetAdoptApiConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient).build()

        return retrofit.create(PetAdoptApi::class.java)
    }


    @Provides
    fun provideSearchPetsCommands(api: PetAdoptApi): SearchPetsCommands = SearchPetsCommands(api)

    @Provides
    fun provideAuthCommands(api: PetAdoptTokenApi): AuthCommands = AuthCommands(api)
}