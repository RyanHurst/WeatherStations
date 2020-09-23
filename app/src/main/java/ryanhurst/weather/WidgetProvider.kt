package ryanhurst.weather

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.IOException

/**
 * AppWidgetProvider for a basic widget that displays information about a small amount of stations
 * Created by ryan on 1/9/17.
 */
class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
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


        fun updateView(response: WeatherResponse?) {
            if (response?.STATION != null && response.STATION.size > 0) {
                var station: WeatherResponse.Station = response.STATION[0]
                views.setTextViewText(R.id.name_1, station.NAME)
                station.OBSERVATIONS?.air_temp_value_1?.value?.let {
                    views.setTextViewText(R.id.temperature_1, getTempString(it))
                }
                station.OBSERVATIONS?.wind_gust_value_1?.value?.let {
                    views.setTextViewText(R.id.wind_gust_1, getWindString(it))
                }

                if (response.STATION.size > 1) {
                    station = response.STATION[1]
                    views.setTextViewText(R.id.name_2, station.NAME)
                    station.OBSERVATIONS?.air_temp_value_1?.value?.let {
                        views.setTextViewText(R.id.temperature_2, getTempString(it))
                    }
                    station.OBSERVATIONS?.wind_gust_value_1?.value?.let {
                        views.setTextViewText(R.id.wind_gust_2, getWindString(it))
                    }
                }

                // Tell the AppWidgetManager to perform an update on the current app widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }

        GlobalScope.launch {
            try {
                val weatherResponse = getSimpleConditions(SHORT_STATIONS_LIST.map { it.id })
                updateView(weatherResponse)
            } catch (e: IOException) {
                updateView(null)
            }
        }
    }
}
