package ryanhurst.weather.application

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.IOException
import ryanhurst.weather.R
import ryanhurst.weather.SHORT_STATIONS_LIST
import ryanhurst.weather.domain.StationObservation
import ryanhurst.weather.domain.WeatherRepository
import javax.inject.Inject

/**
 * AppWidgetProvider for a basic widget that displays information about a small amount of stations
 * Created by ryan on 1/9/17.
 */
@AndroidEntryPoint
class WidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var weatherRepository: WeatherRepository

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val appWidgetId = appWidgetIds[0]

        // Get the layout for the App Widget and attach an on-click listener
        // to the button

        val views = RemoteViews(context.packageName, R.layout.widget)

        // Create an Intent to launch MainActivity
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // Get the layout for the App Widget and attach an on-click listener
        // to the button
        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)


        fun updateView(stationObservations: List<StationObservation>) {
            if (stationObservations.isNotEmpty()) {
                stationObservations.first().let { stationObservation ->
                    views.setTextViewText(R.id.name_1, stationObservation.name)
                    views.setTextViewText(R.id.temperature_1, stationObservation.airTemperature)
                    views.setTextViewText(R.id.wind_gust_1, stationObservation.windGust)
                }

                stationObservations.getOrNull(1)?.let { station ->
                    views.setTextViewText(R.id.name_2, station.name)
                    views.setTextViewText(R.id.temperature_2, station.airTemperature)
                    views.setTextViewText(R.id.wind_gust_2, station.windGust)
                }

                // Tell the AppWidgetManager to perform an update on the current app widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }

        GlobalScope.launch {
            try {
                val weatherResponse =
                    weatherRepository.getSimpleConditions(SHORT_STATIONS_LIST.map { it.id })
                updateView(weatherResponse)
            } catch (e: IOException) {
                updateView(emptyList())
            }
        }
    }
}
