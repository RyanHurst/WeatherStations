package ryanhurst.weather.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ryanhurst.weather.domain.LoadConditionsUseCase
import ryanhurst.weather.domain.WeatherViewState
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val loadConditionsUseCase: LoadConditionsUseCase
) : ViewModel() {

    val weatherViewState = MutableStateFlow<WeatherViewState>(WeatherViewState.Success(emptyList()))

    fun onSettingsChanged() {
        load(true)
    }

    fun onCreate() {
        load(false)
    }

    fun onRefresh() {
        load(true)
    }

    private fun load(forceRefresh: Boolean) {
        if (weatherViewState.value.stationObservations.isEmpty() || forceRefresh) {
            loadConditionsUseCase.loadConditions().onEach {
                weatherViewState.emit(it)
            }.launchIn(viewModelScope)
        }
    }
}
