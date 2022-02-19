package ryanhurst.weather.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ryanhurst.weather.domain.WeatherRepository


class WeatherRepositoryImpl(private val weatherService: WeatherService): WeatherRepository {
    override suspend fun getWeather(stations: List<String>): WeatherResponse = withContext(Dispatchers.IO) {
        weatherService.getWeather(
            stations,
            TOKEN,
            "120",
            listOf(AIR_TEMP, WIND_SPEED, WIND_GUST, WIND_CARDINAL_DIRECTION)
        )
    }

    override suspend fun getSimpleConditions(stations: List<String>): WeatherResponse = withContext(Dispatchers.IO) {
        weatherService.getWeather(
            stations,
            TOKEN,
            "120",
            listOf(AIR_TEMP, WIND_GUST)
        )
    }
}

