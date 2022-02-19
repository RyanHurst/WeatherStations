package ryanhurst.weather.domain

import ryanhurst.weather.data.WeatherResponse
import java.io.IOException
import javax.inject.Inject


class LoadConditionsUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(stations: List<String>): WeatherResponse? {
        return if (stations.isEmpty()) {
            null
        } else {
            try {
                weatherRepository.getWeather(stations)
            } catch (e: IOException) {
                null
            }
        }
    }
}

