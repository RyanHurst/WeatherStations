package ryanhurst.weather.domain

import javax.inject.Inject


class LoadSettingsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke() : List<StationSetting> {
        return settingsRepository.getStationSettings()
    }
}

