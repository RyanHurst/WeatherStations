package ryanhurst.weather.domain


interface SettingsRepository {
    suspend fun getStationSettings(): List<StationSetting>
    suspend fun setStationSettings(stationSettings: List<StationSetting>)
}
