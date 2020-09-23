package ryanhurst.weather

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Utility extension functions
 * Created by ryan on 1/9/17.
 */
private fun createRestService() : Network {
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

    return retrofit.create(Network::class.java)
}

suspend fun getWeather(stations: List<String>): WeatherResponse {
    return createRestService().getWeather(
        stations,
        TOKEN,
        "120",
        listOf(AIR_TEMP, WIND_SPEED, WIND_GUST, WIND_CARDINAL_DIRECTION)
    )
}

suspend fun getSimpleConditions(stations: List<String>): WeatherResponse {
    return createRestService().getWeather(
        stations,
        TOKEN,
        "120",
        listOf(AIR_TEMP, WIND_GUST)
    )
}
