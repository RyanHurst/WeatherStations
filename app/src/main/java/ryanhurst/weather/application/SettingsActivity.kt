package ryanhurst.weather.application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ryanhurst.weather.STATIONS_LIST
import ryanhurst.weather.domain.StationPreference
import ryanhurst.weather.databinding.ActivitySettingsBinding
import ryanhurst.weather.databinding.SettingsRowBinding

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    val stationPreferences = ArrayList<StationPreference>()

    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stationPreferences.addAll(getStationPreferences())
        binding.settingsRecycler.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.settingsRecycler.adapter = SettingsAdapter()
        setResult(RESULT_CANCELED)
    }

    internal inner class SettingsAdapter : RecyclerView.Adapter<StationViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
            return StationViewHolder(
                SettingsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

        override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
            holder.bind(stationPreferences[position])
        }

        override fun getItemCount(): Int {
            return STATIONS_LIST.size
        }
    }

    internal inner class StationViewHolder(private val binding: SettingsRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var station: StationPreference? = null

        init {
            binding.stationBox.setOnClickListener {
                station?.enabled = (it as CompoundButton).isChecked
                setResult(RESULT_OK)
                setStationPreferences(stationPreferences)
            }
        }

        fun bind(station: StationPreference) {
            this.station = station
            binding.stationBox.text = station.displayableName
            binding.stationBox.isChecked = station.enabled
        }
    }
}