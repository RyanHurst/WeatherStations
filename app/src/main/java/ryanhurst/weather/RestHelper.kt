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
    logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
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

suspend fun getWeather(stations: Array<String>): WeatherResponse {
    return createRestService().getWeatherK(
        stations,
        TOKEN,
        "120",
        arrayOf(AIR_TEMP, WIND_SPEED, WIND_GUST, WIND_CARDINAL_DIRECTION)
    )
}

suspend fun getSimpleConditions(stations: Array<String>): WeatherResponse {
    return createRestService().getWeatherK(
        stations,
        TOKEN,
        "120",
        arrayOf(AIR_TEMP, WIND_GUST)
    )
}

fun getTempString(temperatureCelsius: Double): String {
    return celsiusToFahrenheit(temperatureCelsius).toInt().toString() + "°F"
}

fun getWindString(windGustMs: Double): String {
    return msToMph(windGustMs).toInt().toString() + " mph"
}

fun celsiusToFahrenheit(celsius: Double): Double {
    return Math.round(celsius * 1.8 + 32).toDouble()
}
fun msToMph(ms: Double): Double {
    return Math.round(2.237 * ms).toDouble()
}
