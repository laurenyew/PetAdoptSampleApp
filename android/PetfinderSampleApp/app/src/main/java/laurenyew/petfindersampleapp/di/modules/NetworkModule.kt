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
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJDcDFnTmw5ekxzWHBFeFJWR05jVWRaZFI2RDNpdVI5YkY3cE1JVEg1VG9sMllBNlF5TCIsImp0aSI6ImY1ZjI4M2NkZWRiMTU2MjYzNjFkNGFlZmE4ZWU3NWIxMjhhZGQ3ZTkzODBhZTFkNDllMGQzYTRjMWYwYTk3MzQ3NWJhZjJmZTUyYjI0OWUzIiwiaWF0IjoxNTk3Mjg0NDMzLCJuYmYiOjE1OTcyODQ0MzMsImV4cCI6MTU5NzI4ODAzMywic3ViIjoiIiwic2NvcGVzIjpbXX0.LL4I_fAmI6k0AUCzfKT-I93kAaPQbbOEsabuKrHc4LIHTv8fS1qnQ37J1S6Q2FbmgDCV4GgYFpAwzfTugsLhrNGB1il3N8uHMjXYxrseoAxE_HIiSnNWWIVH6FPh9-1G47mG8yNSyzIpX_-PDznZvTktYP7oFlBXGimwfVNGl8VuFvvmVJnMQsvvsBCb2l1dTdoHpFNTy8nOQvIROh7UWTNmwJrm4XDaI8WoKN2zvLDsRStlNTDPyuOfnVGoaysbsZeJFwQIACgOgtebZ0TbUbUE0lo3BbQMI-HBJZiQSP64T2RC4uYIcd6PNeystS75MjCJXLG4CoUY6AWNwSLyhA" //TODO: Fill in with your auth token

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