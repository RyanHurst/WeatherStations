package ryanhurst.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.settings_row.view.*

class SettingsActivity : AppCompatActivity() {

    val stationPreferences = ArrayList<StationPreference>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        stationPreferences.addAll(getStationPreferences(this))
        settings_recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        settings_recycler.adapter = SettingsAdapter()
        setResult(RESULT_CANCELED)
    }

    internal inner class SettingsAdapter : RecyclerView.Adapter<StationViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
            return StationViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.settings_row, parent, false)
            )
        }

        override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
            holder.bind(stationPreferences[position])
        }

        override fun getItemCount(): Int {
            return STATIONS_LIST.size
        }
    }

    internal inner class StationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var station : StationPreference? = null

        init {
            itemView.station_box.setOnClickListener {
                station?.enabled = (it as CompoundButton).isChecked
                setResult(RESULT_OK)
                setStationPreferences(this@SettingsActivity, stationPreferences)
            }
        }

        fun bind(station: StationPreference) {
            this.station = station
            itemView.station_box.text = station.displayableName
            itemView.station_box.isChecked = station.enabled
        }
    }
}