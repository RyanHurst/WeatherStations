package ryanhurst.weather.domain

import ryanhurst.weather.data.WeatherResponse


interface WeatherRepository {
    suspend fun getWeather(stations: List<String>): WeatherResponse
    suspend fun getSimpleConditions(stations: List<String>): WeatherResponse
}
