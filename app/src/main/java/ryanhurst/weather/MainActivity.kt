package ryanhurst.weather

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.weather_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Main activity to display a list of station information
 */
class MainActivity : AppCompatActivity() {
    private var weather: WeatherResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipe_refresh_layout.setOnRefreshListener { getWeather() }
        swipe_refresh_layout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary)
        swipe_refresh_layout.setDistanceToTriggerSync(600)

        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        weather = savedInstanceState?.getString(WEATHER_KEY, null)?.run {
            Gson().fromJson(this, WeatherResponse::class.java)
        }
        if (weather == null) getWeather() else showWeather()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(WEATHER_KEY, Gson().toJson(weather))
    }

    private fun getWeather() {
        empty_text_view.visibility = View.GONE
        swipe_refresh_layout.isRefreshing = true

        getWeather(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                Log.d("main", "Retrieved station information")
                weather = response.body()
                showWeather()
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("main", "Failed to retrieve station information. ${t.message}", t)
                weather = null
                showWeather()
            }
        }, STATIONS_ARRAY)
    }

    private fun showWeather() {
        swipe_refresh_layout.isRefreshing = false
        val size = weather?.STATION?.size ?: 0
        if (size == 0) {
            empty_text_view.visibility = View.VISIBLE
        } else {
            empty_text_view.visibility = View.GONE
        }

        recycler_view.adapter = WeatherAdapter(weather)
    }

    internal inner class WeatherAdapter(private val weatherResponse: WeatherResponse?) :
        RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherAdapter.WeatherViewHolder {
            return WeatherViewHolder(
                layoutInflater.inflate(R.layout.weather_row, parent, false)
            )
        }

        override fun onBindViewHolder(holder: WeatherAdapter.WeatherViewHolder, position: Int) {
            weatherResponse?.STATION?.get(position)?.let { station: WeatherResponse.Station ->
                holder.bind(station)
            } ?: Log.e("main", "unable to bind item")
        }

        override fun getItemCount(): Int {
            return weatherResponse?.STATION?.size ?: 0
        }

        internal inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(station: WeatherResponse.Station) {
                itemView.text_name.text = station.NAME
                val elevationString = "${station.ELEVATION}'"
                itemView.text_elevation.text = elevationString
                itemView.text_temperature.text = station.OBSERVATIONS?.air_temp_value_1?.value?.let {
                    getTempString(it)
                }

                if (station.OBSERVATIONS?.wind_speed_value_1?.value != null) {
                    var windString = station.OBSERVATIONS.wind_cardinal_direction_value_1d?.value ?: ""
                    windString += " " + getWindString(station.OBSERVATIONS.wind_speed_value_1.value)
                    itemView.text_wind_speed.text = windString
                    itemView.wind_speed_container.visibility = View.VISIBLE
                } else {
                    itemView.wind_speed_container.visibility = View.GONE
                }

                if (station.OBSERVATIONS?.wind_gust_value_1?.value != null) {
                    val gustString = getWindString(station.OBSERVATIONS.wind_gust_value_1.value)
                    itemView.text_wind_gust.text = gustString
                    itemView.wind_gust_container.visibility = View.VISIBLE
                } else {
                    itemView.wind_gust_container.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        private const val WEATHER_KEY = "weather"
    }
}
