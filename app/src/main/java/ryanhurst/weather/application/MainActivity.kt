package ryanhurst.weather.application

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import dagger.hilt.android.AndroidEntryPoint
import ryanhurst.weather.R
import ryanhurst.weather.application.composables.Conditions
import ryanhurst.weather.application.theme.ComposeAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private val settingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onReturnFromSettings)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
        setContent {
            ComposeAppTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                )
                            },
                            actions = {
                                IconButton(onClick = ::openSettings) {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = stringResource(R.string.settings)
                                    )
                                }
                            }
                        )
                    },
                ) { innerPadding ->
                    Conditions(
                        viewModel = viewModel, modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun openSettings() {
        settingsLauncher.launch(Intent(this, SettingsActivity::class.java))
    }

    private fun onReturnFromSettings(activityResult: ActivityResult) {
        if (activityResult.resultCode == RESULT_OK) {
            viewModel.onSettingsChanged()
        }
    }
}