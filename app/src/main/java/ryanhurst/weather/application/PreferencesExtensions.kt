package ryanhurst.weather.application

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import ryanhurst.weather.STATIONS_LIST
import ryanhurst.weather.STATIONS_LIST_DEFAULT
import ryanhurst.weather.domain.StationPreference

private const val STATION_PREFERENCES_KEY = "STATION_PREFERENCES_KEY"

private fun Context.getPreferences(): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(this)
}

fun Context.getStationPreferences(): List<StationPreference> {
    val activatedStationIds = getPreferences()
        .getStringSet(STATION_PREFERENCES_KEY, null) ?: STATIONS_LIST_DEFAULT.toSet().map { it.id }

    return STATIONS_LIST.map { stationName ->
        StationPreference(
            enabled = activatedStationIds.contains(stationName.id),
            id = stationName.id,
            displayableName = stationName.displayableName
        )
    }
}

fun Context.getEnabledStationNames(): List<String> {
    return getStationPreferences().filter { it.enabled }.map { it.id }
}

fun Context.setStationPreferences(stationPreferences: List<StationPreference>) {
    getPreferences().edit()
        .putStringSet(
            STATION_PREFERENCES_KEY,
            stationPreferences
                .filter { it.enabled }
                .map { it.id }
                .toSet()
        )
        .apply()
}

