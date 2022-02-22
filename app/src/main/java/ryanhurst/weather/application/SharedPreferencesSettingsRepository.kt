package ryanhurst.weather.application

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import ryanhurst.weather.STATIONS_LIST
import ryanhurst.weather.STATIONS_LIST_DEFAULT
import ryanhurst.weather.domain.SettingsRepository
import ryanhurst.weather.domain.StationSetting

private const val STATION_PREFERENCES_KEY = "STATION_PREFERENCES_KEY"

class SharedPreferencesSettingsRepository(val context: Context): SettingsRepository {

    private fun getPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    override suspend fun getStationSettings(): List<StationSetting> {
        val activatedStationIds = getPreferences()
            .getStringSet(STATION_PREFERENCES_KEY, null) ?: STATIONS_LIST_DEFAULT.toSet().map { it.id }

        return STATIONS_LIST.map { stationName ->
            StationSetting(
                enabled = activatedStationIds.contains(stationName.id),
                id = stationName.id,
                displayableName = stationName.displayableName
            )
        }
    }

    override suspend fun setStationSettings(stationSettings: List<StationSetting>) {
        getPreferences().edit()
            .putStringSet(
                STATION_PREFERENCES_KEY,
                stationSettings
                    .filter { it.enabled }
                    .map { it.id }
                    .toSet()
            )
            .apply()
    }
}

