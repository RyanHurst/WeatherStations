package ryanhurst.weather.data

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

interface WeatherService {
    @GET("stations/nearesttime")
    suspend fun getWeather(
        @Query("stid") stid: List<String>,
        @Query("token") token: String, @Query("within") within: String,
        @Query("vars") vars: List<String>
    ): WeatherResponseDto
}