package ryanhurst.weather.domain

import javax.inject.Inject


class UpdateSettingUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(stationSetting: StationSetting) {
        val settings = settingsRepository.getStationSettings()
        settings.first { it.id == stationSetting.id }.enabled = stationSetting.enabled
        settingsRepository.setStationSettings(settings)
    }
}

