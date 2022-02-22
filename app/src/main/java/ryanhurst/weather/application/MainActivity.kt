package ryanhurst.weather.application

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import ryanhurst.weather.*
import ryanhurst.weather.databinding.ActivityMainBinding
import ryanhurst.weather.databinding.WeatherRowBinding
import ryanhurst.weather.domain.StationObservation
import ryanhurst.weather.domain.WeatherViewState

private const val SETTINGS_REQUEST = 1337
private const val SWIPE_TRIGGER = 600

/**
 * Main activity to display a list of station information
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            viewModel.weatherViewState.collect {
                showWeather(it)
            }
        }
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
        viewModel.load()
    }

    private fun showWeather(viewState: WeatherViewState) {
        binding.swipeRefreshLayout.isRefreshing = viewState is WeatherViewState.Loading
        binding.emptyTextView.isVisible = viewState is WeatherViewState.Error
        binding.recyclerView.adapter = WeatherAdapter(viewState.stationObservations)
    }

    internal inner class WeatherAdapter(private val stationObservations: List<StationObservation>) :
        RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
            return WeatherViewHolder(
                WeatherRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

        override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
            holder.bind(stationObservations[position])
        }

        override fun getItemCount(): Int {
            return stationObservations.size
        }

        internal inner class WeatherViewHolder(private val binding: WeatherRowBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(station: StationObservation) {
                binding.textName.text = station.name
                binding.textElevation.text = station.elevation
                binding.textTemperature.text = station.airTemperature
                binding.textWindSpeed.text = getString(R.string.wind_speed_template, station.windDirection, station.windSpeed)
                binding.windSpeedContainer.isVisible = station.windSpeed.isNotBlank()
                binding.textWindGust.text = station.windGust
                binding.windGustContainer.isVisible = station.windGust.isNotBlank()
            }
        }
    }
}
