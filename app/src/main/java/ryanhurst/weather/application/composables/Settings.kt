package ryanhurst.weather.application.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ryanhurst.weather.application.SettingsViewModel
import ryanhurst.weather.application.theme.ComposeAppTheme
import ryanhurst.weather.domain.StationSetting

@Composable
fun Settings(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
    updateStation: (station: StationSetting) -> Unit = {},
) {
    val stationSettings by viewModel.stationSettingsLiveData.collectAsState()
    Settings(
        stationSettings = stationSettings,
        modifier = modifier,
        updateStation = updateStation
    )
}

@Composable
fun Settings(
    stationSettings: List<StationSetting>,
    modifier: Modifier = Modifier,
    updateStation: (station: StationSetting) -> Unit = {}
) {
    LazyColumn(modifier) {
        stationSettings.forEach { stationSetting ->
            item(stationSetting.id) {
                StationSettingRow(stationSetting = stationSetting, updateStation)
            }
        }
    }
}

@Composable
fun StationSettingRow(
    stationSetting: StationSetting,
    updateStation: (station: StationSetting) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clickable {
                updateStation(stationSetting.copy(enabled = !stationSetting.enabled))
            }
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Checkbox(
            checked = stationSetting.enabled,
            onCheckedChange = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp)
        )
        Text(
            text = stationSetting.displayableName,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    ComposeAppTheme {
        Settings(
            listOf(
                StationSetting(
                    false, "1", "Strawberry Top\n another row\n another one"
                ),
                StationSetting(
                    true, "2", "Base"
                ),
                StationSetting(
                    false, "3", "Super long name of a station that wraps at the end"
                ),
            )
        )
    }
}