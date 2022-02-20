package ryanhurst.weather.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ryanhurst.weather.domain.LoadConditionsUseCase
import ryanhurst.weather.domain.WeatherViewState
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val loadConditionsUseCase: LoadConditionsUseCase) :
    ViewModel() {
    val weatherViewState = MutableStateFlow<WeatherViewState>(WeatherViewState.Success(emptyList()))
    fun load(stations: List<String>) {
        viewModelScope.launch {
            loadConditionsUseCase.loadConditions(stations).collectLatest {
                weatherViewState.emit(it)
            }
        }
    }
}
