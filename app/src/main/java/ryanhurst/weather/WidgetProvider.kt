package ryanhurst.weather

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by ryan on 1/9/17.
 */

class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val appWidgetId = appWidgetIds[0]

        // Get the layout for the App Widget and attach an on-click listener
        // to the button

        val views = RemoteViews(context.packageName, R.layout.widget)

        // Create an Intent to launch ExampleActivity
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // Get the layout for the App Widget and attach an on-click listener
        // to the button
        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)


        fun updateView(response: WeatherResponse?) {
            if (response?.STATION != null && response.STATION!!.size > 0) {
                var station: WeatherResponse.Station = response.STATION!![0]
                views.setTextViewText(R.id.name_1, station.NAME)
                if (station.OBSERVATIONS!!.air_temp_value_1 != null)
                    views.setTextViewText(R.id.temperature_1, getTempString(station.OBSERVATIONS!!.air_temp_value_1!!.value))
                if (station.OBSERVATIONS!!.wind_gust_value_1 != null)
                    views.setTextViewText(R.id.wind_gust_1, getWindString(station.OBSERVATIONS!!.wind_gust_value_1!!.value))

                if (response.STATION!!.size > 1) {
                    station = response.STATION!![1]
                    views.setTextViewText(R.id.name_2, station.NAME)
                    if (station.OBSERVATIONS!!.air_temp_value_1 != null)
                        views.setTextViewText(R.id.temperature_2, getTempString(station.OBSERVATIONS!!.air_temp_value_1!!.value))
                    if (station.OBSERVATIONS!!.wind_gust_value_1 != null)
                        views.setTextViewText(R.id.wind_gust_2, getWindString(station.OBSERVATIONS!!.wind_gust_value_1!!.value))
                }

                // Tell the AppWidgetManager to perform an update on the current app widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }

        getSimpleConditions(object: Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                Log.d("main", "success!")
                updateView(response.body())
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("main", "failure!", t)
                updateView(null)
            }
        }, SHORT_STATIONS_ARRAY)
    }
}