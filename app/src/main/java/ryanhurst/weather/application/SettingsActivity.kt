package ryanhurst.weather.application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ryanhurst.weather.STATIONS_LIST
import ryanhurst.weather.domain.StationSetting
import ryanhurst.weather.databinding.ActivitySettingsBinding
import ryanhurst.weather.databinding.SettingsRowBinding

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    val viewModel: SettingsViewModel by viewModels()

    lateinit var binding: ActivitySettingsBinding

    private val adapter = SettingsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.settingsRecycler.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.settingsRecycler.adapter = adapter
        setResult(RESULT_CANCELED)
        viewModel.stationSettingsLiveData.observe(this, adapter::setSettings)
    }

    internal inner class SettingsAdapter : RecyclerView.Adapter<StationViewHolder>() {
        private val stationSettings: MutableList<StationSetting> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
            return StationViewHolder(
                SettingsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

        override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
            holder.bind(stationSettings[position])
        }

        override fun getItemCount(): Int {
            return STATIONS_LIST.size
        }

        fun setSettings(stationSettings: List<StationSetting>) {
            this.stationSettings.clear()
            this.stationSettings.addAll(stationSettings)
            notifyDataSetChanged()
        }
    }

    internal inner class StationViewHolder(private val binding: SettingsRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var station: StationSetting

        init {
            binding.stationBox.setOnClickListener {
                station.enabled = (it as CompoundButton).isChecked
                setResult(RESULT_OK)
                viewModel.onUpdateSetting(station)
            }
        }

        fun bind(station: StationSetting) {
            this.station = station
            binding.stationBox.text = station.displayableName
            binding.stationBox.isChecked = station.enabled
        }
    }
}