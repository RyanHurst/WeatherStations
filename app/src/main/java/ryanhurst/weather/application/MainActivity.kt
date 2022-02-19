package ryanhurst.weather.application

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ryanhurst.weather.*
import ryanhurst.weather.data.WeatherResponse
import ryanhurst.weather.databinding.ActivityMainBinding
import ryanhurst.weather.databinding.WeatherRowBinding
import ryanhurst.weather.domain.getEnabledStationNames
import ryanhurst.weather.domain.getTempString
import ryanhurst.weather.domain.getWindString

private const val SETTINGS_REQUEST = 1337
private const val SWIPE_TRIGGER = 600

/**
 * Main activity to display a list of station information
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val model: WeatherViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model.weatherLiveData.observe(this) { showWeather(it) }
        binding.swipeRefreshLayout.setOnRefreshListener { getWeather() }
        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.colorAccent,
            R.color.colorPrimaryDark,
            R.color.colorPrimary
        )
        binding.swipeRefreshLayout.setDistanceToTriggerSync(SWIPE_TRIGGER)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        savedInstanceState ?: getWeather()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_weather, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            openSettings()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == SETTINGS_REQUEST) {
            getWeather()
        }
    }

    private fun openSettings() {
        startActivityForResult(Intent(this, SettingsActivity::class.java), SETTINGS_REQUEST)
    }

    private fun getWeather() {
        binding.emptyTextView.visibility = View.GONE
        binding.swipeRefreshLayout.isRefreshing = true
        model.load(getEnabledStationNames(this))
    }

    private fun showWeather(weatherResponse: WeatherResponse?) {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.emptyTextView.visibility = if (weatherResponse?.STATION.isNullOrEmpty()) View.VISIBLE else View.GONE
        binding.recyclerView.adapter = WeatherAdapter(weatherResponse)
    }

    internal inner class WeatherAdapter(private val weatherResponse: WeatherResponse?) :
        RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
            return WeatherViewHolder(
                WeatherRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

        override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
            weatherResponse?.STATION?.get(position)?.let { station: WeatherResponse.Station ->
                holder.bind(station)
            } ?: Log.e("main", "unable to bind item")
        }

        override fun getItemCount(): Int {
            return weatherResponse?.STATION?.size ?: 0
        }

        internal inner class WeatherViewHolder(private val binding: WeatherRowBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(station: WeatherResponse.Station) {
                binding.textName.text = station.NAME
                val elevationString = "${station.ELEVATION}'"
                binding.textElevation.text = elevationString
                binding.textTemperature.text = station.OBSERVATIONS?.air_temp_value_1?.value?.let {
                    getTempString(it)
                }

                if (station.OBSERVATIONS?.wind_speed_value_1?.value != null) {
                    var windString = station.OBSERVATIONS.wind_cardinal_direction_value_1d?.value ?: ""
                    windString += " " + getWindString(station.OBSERVATIONS.wind_speed_value_1.value)
                    binding.textWindSpeed.text = windString
                    binding.windSpeedContainer.visibility = View.VISIBLE
                } else {
                    binding.windSpeedContainer.visibility = View.GONE
                }

                if (station.OBSERVATIONS?.wind_gust_value_1?.value != null) {
                    val gustString = getWindString(station.OBSERVATIONS.wind_gust_value_1.value)
                    binding.textWindGust.text = gustString
                    binding.windGustContainer.visibility = View.VISIBLE
                } else {
                    binding.windGustContainer.visibility = View.GONE
                }
            }
        }
    }
}
