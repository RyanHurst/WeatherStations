package ryanhurst.weather

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

private val STATION_PREFERENCES_KEY = "STATION_PREFERENCES_KEY"

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

private fun getPreferences(activity: Activity): SharedPreferences {
    return activity.getPreferences(Context.MODE_PRIVATE)
}

fun getStationPreferences(activity: Activity): List<StationPreference> {
    val activatedStations = getPreferences(activity)
        .getStringSet(STATION_PREFERENCES_KEY, null) ?: STATIONS_ARRAY_DEFAULT.toSet()

    return STATIONS_ARRAY.map {
        StationPreference(activatedStations.contains(it), it)
    }
}

fun setStationPreferences(activity: Activity, stationPreferences: List<StationPreference>) {
    getPreferences(activity).edit()
        .putStringSet(
            STATION_PREFERENCES_KEY,
            stationPreferences
                .filter { it.enabled }
                .map { it.name }
                .toSet()
        )
        .apply()
}