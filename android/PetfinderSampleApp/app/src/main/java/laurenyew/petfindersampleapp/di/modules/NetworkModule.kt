package laurenyew.petfindersampleapp.di.modules

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
    private val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJDcDFnTmw5ekxzWHBFeFJWR05jVWRaZFI2RDNpdVI5YkY3cE1JVEg1VG9sMllBNlF5TCIsImp0aSI6ImQ2OTRlZWYyOGYxODNlZmYxMWEzMGFhN2FiNTczZDQ2ZjY0NDRmMjUzMmJkMjU5ZjYzNTc5M2ZlYzk0ODU2MjQ5MTliZTI1YTRjNDA4MWExIiwiaWF0IjoxNTkzMjI5OTEwLCJuYmYiOjE1OTMyMjk5MTAsImV4cCI6MTU5MzIzMzUxMCwic3ViIjoiIiwic2NvcGVzIjpbXX0.PNwQ6GaMhBd50shppngNTYr-dUCwbLJYnds0yZF7chjBOOoL2eKstKOxWGQSbaL9ZE75yx7z5u5rd26ch1JJn8YYbtY81JuOEQoIoz4wToFxGbCvzlNZx-CEXZPzSpiAh_IWVtm8Fec8VLAUexDVwSHx_emVEXlAk7h81ikc0IeAXCJwH9CNxAts1opzxAeGhxXH6wPB-9YT596a0AZRLhRpGAJU-Dlb-JUxbB8OyM1EAMavmKRJS-m_fIPZpOtp5XqIOLo7CLJwBUV5_ZaInuDowmpzBfMJzseLTvByzutT2OlI_tDKX7M5JWjb2hsxeXLbRtCKgPHVlm0DZcTT5Q" //TODO: Fill in with your auth token

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
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
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