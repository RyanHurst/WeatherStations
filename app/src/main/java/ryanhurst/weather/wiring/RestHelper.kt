package ryanhurst.weather.wiring

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ryanhurst.weather.data.WeatherService

/**
 * Utility extension functions
 * Created by ryan on 1/9/17.
 */
private fun createRestService() : WeatherService {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.mesowest.net/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    return retrofit.create(WeatherService::class.java)
}


