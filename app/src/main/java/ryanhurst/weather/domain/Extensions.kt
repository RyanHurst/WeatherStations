package ryanhurst.weather.domain

import android.app.Activity
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import ryanhurst.weather.STATIONS_LIST
import ryanhurst.weather.STATIONS_LIST_DEFAULT

private const val STATION_PREFERENCES_KEY = "STATION_PREFERENCES_KEY"

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
    return PreferenceManager.getDefaultSharedPreferences(activity)
}

fun getStationPreferences(activity: Activity): List<StationPreference> {
    val activatedStationIds = getPreferences(activity)
        .getStringSet(STATION_PREFERENCES_KEY, null) ?: STATIONS_LIST_DEFAULT.toSet().map { it.id }

    return STATIONS_LIST.map { stationName ->
        StationPreference(
            enabled = activatedStationIds.contains(stationName.id),
            id = stationName.id,
            displayableName = stationName.displayableName
        )
    }
}

fun getEnabledStationNames(activity: Activity): List<String> {
    return getStationPreferences(activity).filter { it.enabled }.map { it.id }
}

fun setStationPreferences(activity: Activity, stationPreferences: List<StationPreference>) {
    getPreferences(activity).edit()
        .putStringSet(
            STATION_PREFERENCES_KEY,
            stationPreferences
                .filter { it.enabled }
                .map { it.id }
                .toSet()
        )
        .apply()
}