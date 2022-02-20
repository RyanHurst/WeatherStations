package ryanhurst.weather.domain

interface WeatherRepository {
    suspend fun getWeather(stations: List<String>): List<StationObservation>
    suspend fun getSimpleConditions(stations: List<String>): List<StationObservation>
}
