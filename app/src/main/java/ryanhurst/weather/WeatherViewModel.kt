package ryanhurst.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    val weatherLiveData = MutableLiveData<WeatherResponse>()
    fun getWeather2(stations: Array<String>) {
        viewModelScope.launch {
            weatherLiveData.postValue(getWeather(stations))
        }
    }
}
