package ryanhurst.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface to work with RetroFit. see https://square.github.io/retrofit/
 * Created by ryan on 1/8/17.
 */
const val WIND_SPEED = "wind_speed"
const val AIR_TEMP = "air_temp"
const val WIND_GUST = "wind_gust"
const val WIND_DIRECTION = "wind_direction"
const val WIND_CARDINAL_DIRECTION = "wind_cardinal_direction"

interface Network {

    // TODO: 9/21/2020 remove this one
    @GET("stations/nearesttime")
    fun getWeather(@Query("stid") stid: Array<String>,
                   @Query("token") token: String, @Query("within") within: String,
                   @Query("vars") vars: Array<String>): Call<WeatherResponse>

    @GET("stations/nearesttime")
    suspend fun getWeatherK(@Query("stid") stid: Array<String>,
                   @Query("token") token: String, @Query("within") within: String,
                   @Query("vars") vars: Array<String>): WeatherResponse

}
