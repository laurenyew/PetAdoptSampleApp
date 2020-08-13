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
    private val authToken =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJDcDFnTmw5ekxzWHBFeFJWR05jVWRaZFI2RDNpdVI5YkY3cE1JVEg1VG9sMllBNlF5TCIsImp0aSI6ImFjOGIxYzQ4Y2I4MWM2MTUyNDE5OWJiNzU5ZWYwNGUwMzdjZDI2NGM5ZmYzYjYyMzczZjZlNDc0NjZmMDE4Y2I0MTk1NGFmODg2MDRiYjJiIiwiaWF0IjoxNTk3Mjg4NDUyLCJuYmYiOjE1OTcyODg0NTIsImV4cCI6MTU5NzI5MjA1Miwic3ViIjoiIiwic2NvcGVzIjpbXX0.knQqKxqYxdmFCUuhVh4PaSG44tWljFTKBT_KFCHA9M9RId3LU5kyrH0f8ymr_V5wNeSHA-ZOk4p8VYufQPtYJOefg8uyVzuxxgeoinEr9JOBXuve-vha6EwlEkzs7HqMFvSfX1TV3i67RKC42JB-is2pixcabh3jUXPhW1ry6m0Ukh_GusQYG-zaeYcgBIK9SnSH9L_sIo-03uNfCnIBGGbytKhOHkDSxbN0Aj4WL9SA7oYhKAl8Q3beAeGybONsXbV_HBbDd_j3vPpaETM9E9S2y-WPqhy4sOfmmIPlJ9xpTI8SN_NnzwXQlWvZghGL5wK51ioPljh2f9jlujH7LQ" //TODO: Fill in with your auth token

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