package laurenyew.petfindersampleapp.networking

import androidx.annotation.VisibleForTesting
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

object PetFinderApiBuilder {
    private const val AUTH_TOKEN = "" //TODO: Fill in with your auth token
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val okHttpClient: OkHttpClient.Builder
    private val retrofit: Retrofit

    init {
        okHttpClient = setupOkHttp()
        retrofit = Retrofit.Builder().baseUrl(PetfinderApiConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient.build()).build()
    }

    fun <T> apiBuilder(apiClazz: Class<T>): T? = retrofit.create(apiClazz)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setupOkHttp(): OkHttpClient.Builder {
        //Setup HttpClient
        val httpClientBuilder = OkHttpClient.Builder()

        //Add headers
        httpClientBuilder.addInterceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("Accept-Language", Locale.getDefault().language)
                .addHeader("Authorization", "Bearer $AUTH_TOKEN")
                .build()
            it.proceed(request)
        }
        return httpClientBuilder
    }
}