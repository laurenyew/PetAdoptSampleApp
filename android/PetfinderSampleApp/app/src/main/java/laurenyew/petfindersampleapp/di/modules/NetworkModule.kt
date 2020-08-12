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
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJDcDFnTmw5ekxzWHBFeFJWR05jVWRaZFI2RDNpdVI5YkY3cE1JVEg1VG9sMllBNlF5TCIsImp0aSI6IjU5OTc5MDBjNzJkNjhiNDM5OTk2NTQ5YjM2MDBkYTFiYjk0MzlhMTJjMTVmNmZjYjJhZmNhNDMwMjk1OTIzYTNiZDA3ZDY3MTdiM2Y2N2U4IiwiaWF0IjoxNTk1OTAyODY3LCJuYmYiOjE1OTU5MDI4NjcsImV4cCI6MTU5NTkwNjQ2Nywic3ViIjoiIiwic2NvcGVzIjpbXX0.bMJtwbhOpWAznsMLTxCA4pHyPMJhnGsuRaYpRu6DQT15eZhWhRLuhcQbknqCxT-MYP3hRNxiruEhnfdK293zyvGcRNdHCUFKBa6-RaSisXuVx--jobLweLtqreCIlrNJijcpGSCGmDFf_bsRpqWXzXS9LdKpVHDo84wkRvbMsVo76PKFTrayWjdfQRzl3kIn_zTJ5uG16Lfl93Hk5GK6_WmQ_m5afFTpePCgG_G439pqOkIQv2vtbOS1EDBtSRykXnPxK7OF5_Nw6WyN2IU_dCbSPPmXraKtA4pExE9_Lj5M4WgIJy5FNgxuyTBY587Ulp4yy0GBuCQiYFHuiRg7EA" //TODO: Fill in with your auth token

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