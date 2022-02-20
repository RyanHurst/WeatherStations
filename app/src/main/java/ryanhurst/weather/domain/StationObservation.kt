package ryanhurst.weather.domain


data class StationObservation(
    val elevation: String,
    val name: String,
    val airTemperature: String,
    val windSpeed: String,
    val windGust: String,
    val windDirection: String,
)
