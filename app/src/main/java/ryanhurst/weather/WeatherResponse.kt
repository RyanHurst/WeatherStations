package ryanhurst.weather

import java.util.ArrayList

/**
 * WeatherResponse object. Holds station data
 * Created by ryan on 1/8/17.
 */
 // todo implement parcelable
class WeatherResponse {

    val STATION: ArrayList<Station>? = null
    val UNITS: Unit? = null

    inner class Unit {
        val air_temp: String? = null
    }

    inner class Station {
        val STATUS: String? = null
        val MNET_ID: String? = null
        val ELEVATION: String? = null
        val NAME: String? = null
        val STID: String? = null
        val STATE: String? = null
        val TIMEZONE: String? = null
        val ID: String? = null
        val LONGITUDE: Double? = null
        val LATITUDE: Double? = null
        val PERIOD_OF_RECORD: Period? = null
        val OBSERVATIONS: Observation? = null

        inner class Period {
            val start: String? = null
            val end: String? = null
        }

        inner class Observation {
            val air_temp_value_1: Value? = null
            val wind_speed_value_1: Value? = null
            val wind_gust_value_1: Value? = null
            val wind_cardinal_direction_value_1d: StringValue? = null

            inner class Value {
                val date_time: String? = null
                val value: Double? = null
            }

            inner class StringValue {
                val date_time: String? = null
                val value: String? = null
            }
        }
    }

}
