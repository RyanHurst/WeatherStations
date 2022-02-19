package ryanhurst.weather.application

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ryanhurst.weather.data.WeatherResponse
import ryanhurst.weather.domain.LoadConditionsUseCase
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val loadConditionsUseCase: LoadConditionsUseCase) :
    ViewModel() {
    val weatherLiveData = MutableLiveData<WeatherResponse?>()
    fun load(stations: List<String>) {
        viewModelScope.launch {
            weatherLiveData.postValue(loadConditionsUseCase(stations))
        }
    }
}
