package ryanhurst.weather.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import dagger.hilt.android.AndroidEntryPoint
import ryanhurst.weather.R
import ryanhurst.weather.application.composables.Settings
import ryanhurst.weather.application.theme.ComposeAppTheme
import ryanhurst.weather.domain.StationSetting

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAppTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(id = R.string.settings),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                )
                            }
                        )
                    },
                ) { innerPadding ->
                    // Screen content
                    Settings(viewModel = viewModel, modifier = Modifier.padding(innerPadding), ::onUpdateStation)
                }
            }
        }
    }

    private fun onUpdateStation(station: StationSetting) {
        setResult(RESULT_OK)
        viewModel.onUpdateSetting(station)
    }
}