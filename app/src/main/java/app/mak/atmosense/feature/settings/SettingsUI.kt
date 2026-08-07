package app.mak.atmosense.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.mak.atmosense.R
import app.mak.atmosense.core.common.model.AppTheme
import app.mak.atmosense.core.common.model.PressureUnit
import app.mak.atmosense.core.common.model.TemperatureUnit
import app.mak.atmosense.core.common.model.WindSpeedUnit
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(SettingsScreen::class, AppScope::class)
@Composable
fun SettingsUI(state: SettingsScreen.State, modifier: Modifier = Modifier) {
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = { Text(stringResource(R.string.settings_title)) },
        navigationIcon = {
          IconButton(onClick = { state.eventSink(SettingsScreen.Event.Back) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
          }
        }
      )
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
    ) {
      item { CategoryTitle(stringResource(R.string.settings_units_category)) }

      item {
        UnitSettingItem(
          title = stringResource(R.string.settings_temperature),
          options = TemperatureUnit.entries,
          selectedOption = state.settings.temperatureUnit,
          onOptionSelected = {
            state.eventSink(SettingsScreen.Event.UpdateSettings(state.settings.copy(temperatureUnit = it)))
          }
        )
      }

      item {
        UnitSettingItem(
          title = stringResource(R.string.settings_wind_speed),
          options = WindSpeedUnit.entries,
          selectedOption = state.settings.windSpeedUnit,
          onOptionSelected = {
            state.eventSink(SettingsScreen.Event.UpdateSettings(state.settings.copy(windSpeedUnit = it)))
          }
        )
      }

      item {
        UnitSettingItem(
          title = stringResource(R.string.settings_pressure),
          options = PressureUnit.entries,
          selectedOption = state.settings.pressureUnit,
          onOptionSelected = {
            state.eventSink(SettingsScreen.Event.UpdateSettings(state.settings.copy(pressureUnit = it)))
          }
        )
      }

      item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }

      item { CategoryTitle(stringResource(R.string.settings_appearance_category)) }

      item {
        UnitSettingItem(
          title = stringResource(R.string.settings_theme),
          options = AppTheme.entries,
          selectedOption = state.settings.appTheme,
          onOptionSelected = {
            state.eventSink(SettingsScreen.Event.UpdateSettings(state.settings.copy(appTheme = it)))
          }
        )
      }

      item {
        ListItem(
          headlineContent = { Text(stringResource(R.string.settings_dynamic_colors)) },
          trailingContent = {
            Switch(
              checked = state.settings.useDynamicColors,
              onCheckedChange = {
                state.eventSink(
                  SettingsScreen.Event.UpdateSettings(
                    state.settings.copy(
                      useDynamicColors = it
                    )
                  )
                )
              }
            )
          }
        )
      }
    }
  }
}

@Composable
private fun CategoryTitle(title: String) {
  Text(
    text = title,
    style = MaterialTheme.typography.titleSmall,
    color = MaterialTheme.colorScheme.primary,
    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
  )
}

@Composable
private fun <T : Enum<T>> UnitSettingItem(
  title: String,
  options: List<T>,
  selectedOption: T,
  onOptionSelected: (T) -> Unit
) {
  var showDialog by remember { mutableStateOf(false) }

  ListItem(
    headlineContent = { Text(title) },
    supportingContent = { Text(selectedOption.toDisplayName()) },
    modifier = Modifier.clickable { showDialog = true }
  )

  if (showDialog) {
    AlertDialog(
      onDismissRequest = { showDialog = false },
      title = { Text(title) },
      text = {
        Column {
          options.forEach { option ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  onOptionSelected(option)
                  showDialog = false
                }
                .padding(vertical = 12.dp)
            ) {
              RadioButton(
                selected = option == selectedOption,
                onClick = null
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(option.toDisplayName())
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showDialog = false }) {
          Text(stringResource(R.string.dismiss))
        }
      }
    )
  }
}

@Composable
private fun Any.toDisplayName(): String = when (this) {
  TemperatureUnit.CELSIUS -> stringResource(R.string.unit_celsius)
  TemperatureUnit.FAHRENHEIT -> stringResource(R.string.unit_fahrenheit)
  TemperatureUnit.KELVIN -> stringResource(R.string.unit_kelvin)

  WindSpeedUnit.METERS_PER_SECOND -> stringResource(R.string.unit_ms)
  WindSpeedUnit.KILOMETERS_PER_HOUR -> stringResource(R.string.unit_kmh)
  WindSpeedUnit.MILES_PER_HOUR -> stringResource(R.string.unit_mph)
  WindSpeedUnit.KNOTS -> stringResource(R.string.unit_kn)
  WindSpeedUnit.FEET_PER_SECOND -> stringResource(R.string.unit_fts)

  PressureUnit.HECTOPASCAL -> stringResource(R.string.unit_hpa)
  PressureUnit.KILOPASCAL -> stringResource(R.string.unit_kpa)
  PressureUnit.MILLIBAR -> stringResource(R.string.unit_mbar)
  PressureUnit.ATMOSPHERE -> stringResource(R.string.unit_atm)
  PressureUnit.MILLIMETERS_OF_MERCURY -> stringResource(R.string.unit_mmhg)
  PressureUnit.INCHES_OF_MERCURY -> stringResource(R.string.unit_inhg)

  AppTheme.SYSTEM -> stringResource(R.string.theme_system)
  AppTheme.LIGHT -> stringResource(R.string.theme_light)
  AppTheme.DARK -> stringResource(R.string.theme_dark)

  else -> this.toString()
}
