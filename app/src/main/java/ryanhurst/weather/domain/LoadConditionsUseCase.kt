package ryanhurst.weather.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class LoadConditionsUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository,
) {

    fun loadConditions(): Flow<WeatherViewState> = flow {
        emit(WeatherViewState.Loading)
        val stations = settingsRepository.getStationSettings().filter { it.enabled }.map { it.id }
        if (stations.isEmpty()) {
            emit(WeatherViewState.Error.NoStationsError)
        } else {
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

