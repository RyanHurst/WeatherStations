package ryanhurst.weather.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ryanhurst.weather.domain.LoadSettingsUseCase
import ryanhurst.weather.domain.StationSetting
import ryanhurst.weather.domain.UpdateSettingUseCase
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val loadSettingsUseCase: LoadSettingsUseCase,
    private val updateSettingUseCase: UpdateSettingUseCase,
): ViewModel() {

    private val _stationSettingsLiveData = MutableStateFlow<List<StationSetting>>(emptyList())
    val stationSettingsLiveData: StateFlow<List<StationSetting>> = _stationSettingsLiveData

    init {
        viewModelScope.launch {
            _stationSettingsLiveData.emit(loadSettingsUseCase.invoke())
        }
    }

    fun onUpdateSetting(setting: StationSetting) {
        viewModelScope.launch {
            _stationSettingsLiveData.emit(updateSettingUseCase.invoke(setting))
        }
    }
}

