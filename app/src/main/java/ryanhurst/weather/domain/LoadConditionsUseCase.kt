package ryanhurst.weather.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class LoadConditionsUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    fun loadConditions(stations: List<String>): Flow<WeatherViewState> = flow {
        if (stations.isEmpty()) {
            emit(WeatherViewState.Error.NoStationsError)
        } else {
            emit(WeatherViewState.Loading)
            try {
                emit(WeatherViewState.Success(weatherRepository.getWeather(stations)))
            } catch (e: IOException) {
                emit(WeatherViewState.Error.NoInternetError)
            } catch (e: Exception) {
                emit(WeatherViewState.Error.UnknownError)
            }
        }
    }
}

