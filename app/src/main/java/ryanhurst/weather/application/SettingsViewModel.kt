package ryanhurst.weather.application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ryanhurst.weather.domain.LoadSettingsUseCase
import ryanhurst.weather.domain.UpdateSettingUseCase
import ryanhurst.weather.domain.StationSetting
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val loadSettingsUseCase: LoadSettingsUseCase,
    private val updateSettingUseCase: UpdateSettingUseCase,
): ViewModel() {

    private val _stationSettingsLiveData = MutableLiveData<List<StationSetting>>(emptyList())
    val stationSettingsLiveData: LiveData<List<StationSetting>> = _stationSettingsLiveData

    init {
        viewModelScope.launch {
            _stationSettingsLiveData.value = loadSettingsUseCase.invoke()
        }
    }

    fun onUpdateSetting(setting: StationSetting) {
        viewModelScope.launch {
            updateSettingUseCase.invoke(setting)
        }
    }
}

