package ryanhurst.weather.domain

import kotlin.math.roundToInt

fun Double.toTemperatureString(): String {
    return celsiusToFahrenheit().toInt().toString() + "°F"
}

fun Double.toWindString(): String {
    return msToMph().toInt().toString() + " mph"
}

fun Double.celsiusToFahrenheit(): Double {
    return (this * 1.8 + 32).roundToInt().toDouble()
}

fun Double.msToMph(): Double {
    return (2.237 * this).roundToInt().toDouble()
}