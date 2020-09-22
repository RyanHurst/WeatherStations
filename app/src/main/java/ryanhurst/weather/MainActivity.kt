package ryanhurst.weather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.weather_row.view.*

/**
 * Main activity to display a list of station information
 */
class MainActivity : AppCompatActivity() {
    private val model: WeatherViewModel by viewModels()
    private val SETTINGS_RESULT = 1337

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model.weatherLiveData.observe(this) { showWeather(it) }
        swipe_refresh_layout.setOnRefreshListener { getWeather() }
        swipe_refresh_layout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary)
        swipe_refresh_layout.setDistanceToTriggerSync(600)
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        savedInstanceState ?: getWeather()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            getWeather()
        }
    }

    private fun openSettings() {
        startActivityForResult(Intent(this, SettingsActivity::class.java), SETTINGS_RESULT)
    }

    private fun getWeather() {
        empty_text_view.visibility = View.GONE
        swipe_refresh_layout.isRefreshing = true
        model.load(STATIONS_ARRAY)
    }

    private fun showWeather(weatherResponse: WeatherResponse?) {
        swipe_refresh_layout.isRefreshing = false
        empty_text_view.visibility = if (weatherResponse?.STATION.isNullOrEmpty()) View.VISIBLE else View.GONE
        recycler_view.adapter = WeatherAdapter(weatherResponse)
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
}
