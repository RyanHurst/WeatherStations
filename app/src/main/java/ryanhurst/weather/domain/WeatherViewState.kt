package ryanhurst.weather.domain


sealed class WeatherViewState(val stationObservations: List<StationObservation> = emptyList()) {
    class Success(conditions: List<StationObservation>): WeatherViewState(conditions)
    sealed class Error: WeatherViewState() {
        object NoStationsError: Error()
        object NoInternetError: Error()
        object UnknownError: Error()
    }
    object Loading: WeatherViewState()
}