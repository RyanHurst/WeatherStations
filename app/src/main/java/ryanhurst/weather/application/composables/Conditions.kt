package ryanhurst.weather.application.composables

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ryanhurst.weather.R
import ryanhurst.weather.application.WeatherViewModel
import ryanhurst.weather.application.theme.ComposeAppTheme
import ryanhurst.weather.domain.StationObservation
import ryanhurst.weather.domain.WeatherViewState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Conditions(viewModel: WeatherViewModel, modifier: Modifier) {
    val weatherViewState by viewModel.weatherViewState.collectAsState()
    val state = rememberPullRefreshState(
        refreshing = weatherViewState is WeatherViewState.Loading,
        onRefresh = {
            viewModel.onRefresh()
        })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state),
    ) {
        Conditions(weatherViewState, modifier)
        Error(weatherViewState)
        PullRefreshIndicator(
            refreshing = weatherViewState is WeatherViewState.Loading,
            state = state,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}

@Composable
fun Error(weatherViewState: WeatherViewState) {
    when (weatherViewState) {
        WeatherViewState.Error.NoStationsError -> stringResource(id = R.string.no_stations_error)
        WeatherViewState.Error.UnknownError,
        WeatherViewState.Error.NoInternetError -> stringResource(id = R.string.no_items_found)
        WeatherViewState.Loading,
        is WeatherViewState.Success -> null
    }?.let { errorMessage ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(ScrollState(0))
        ) {
            Text(
                text = errorMessage,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun Conditions(weatherViewState: WeatherViewState, modifier: Modifier) {
    LazyColumn(modifier) {
        weatherViewState.stationObservations.forEach { station ->
            item(station.name) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(vertical = 10.dp)) {
                        Text(
                            text = station.name,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = station.airTemperature,
                        )
                    }
                    Row {
                        Text(
                            text = stringResource(id = R.string.elevation),
                            modifier = Modifier.weight(1f)
                        )
                        Text(text = station.elevation)
                    }
                    Row {
                        Text(
                            text = stringResource(id = R.string.wind_speed),
                            modifier = Modifier.weight(1f)
                        )
                        Text(text = station.windDirection)
                        Text(text = station.windSpeed)
                    }
                    Row {
                        Text(
                            text = stringResource(id = R.string.max_gust),
                            modifier = Modifier.weight(1f)
                        )
                        Text(text = station.windGust)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StationsPreview() {
    ComposeAppTheme {
        Conditions(
            WeatherViewState.Success(
                listOf(
                    StationObservation(
                        elevation = "7402'",
                        name = "SNOWBASIN - MID BOWL",
                        airTemperature = "52F",
                        windSpeed = "8 mph",
                        windGust = "27 mph",
                        windDirection = "NNW"
                    ),
                    StationObservation(
                        elevation = "7402'",
                        name = "SNOWBASIN - BASE",
                        airTemperature = "52F",
                        windSpeed = "8 mph",
                        windGust = "27 mph",
                        windDirection = "NNW"
                    )
                )
            ), Modifier.padding(8.dp)
        )
    }
}