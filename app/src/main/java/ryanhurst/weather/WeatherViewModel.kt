package ryanhurst.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okio.IOException

class WeatherViewModel: ViewModel() {
    val weatherLiveData = MutableLiveData<WeatherResponse>()
    fun getWeather2(stations: Array<String>) {
        viewModelScope.launch {
            try {
                weatherLiveData.postValue(getWeather(stations))
            } catch (e: IOException) {
                weatherLiveData.postValue(null)
            }
        }
    }
}
