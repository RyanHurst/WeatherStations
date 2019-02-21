package ryanhurst.weather

import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by ryan on 1/9/17.
 */

private fun createRestService() : Network {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://api.mesowest.net/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(Network::class.java)
}

fun getWeather(callback: Callback<WeatherResponse>, stations: Array<String>) {
    createRestService().getWeather(
        stations,
        TOKEN,
        "120",
        arrayOf(AIR_TEMP, WIND_SPEED, WIND_GUST, WIND_CARDINAL_DIRECTION))
        .enqueue(callback)
}

fun getSimpleConditions(callback: Callback<WeatherResponse>, stations: Array<String>) {
    createRestService().getWeather(
        stations,
        TOKEN,
        "120",
        arrayOf(AIR_TEMP, WIND_GUST))
        .enqueue(callback)
}

fun getTempString(temperatureCelsius: Double?): String {
    return celsiusToFahrenheit(temperatureCelsius).toInt().toString() + "°F"
}

fun getWindString(windGustMs: Double?): String {
    return msToMph(windGustMs).toInt().toString() + " mph"
}

fun celsiusToFahrenheit(celsius: Double?): Double {
    return Math.round(celsius!! * 1.8 + 32).toDouble()
}

fun msToMph(ms: Double?): Double {
    return Math.round(2.237 * ms!!).toDouble()
}
