package ryanhurst.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by ryan on 1/8/17.
 */
val WIND_SPEED = "wind_speed"
val AIR_TEMP = "air_temp"
val WIND_GUST = "wind_gust"
val WIND_DIRECTION = "wind_direction"
val WIND_CARDINAL_DIRECTION = "wind_cardinal_direction"

interface Network {

    @GET("stations/nearesttime")
    fun getWeather(@Query("stid") stid: Array<String>,
                   @Query("token") token: String, @Query("within") within: String,
                   @Query("vars") vars: Array<String>): Call<WeatherResponse>

}
