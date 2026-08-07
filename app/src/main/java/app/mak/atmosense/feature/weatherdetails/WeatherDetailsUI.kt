package app.mak.atmosense.feature.weatherdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.mak.atmosense.R
import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.ForecastSlot
import app.mak.atmosense.core.common.model.PressureUnit
import app.mak.atmosense.core.common.model.TemperatureUnit
import app.mak.atmosense.core.common.model.UserSettings
import app.mak.atmosense.core.common.model.WindSpeedUnit
import coil3.compose.AsyncImage
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(screen = WeatherDetailsScreen::class, scope = AppScope::class)
@Composable
internal fun WeatherDetailsUI(
  modifier: Modifier = Modifier,
  state: WeatherDetailsScreen.State
) {
  val details = state.details
  val eventSink = state.eventSink
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = { Text(details?.cityName.orEmpty()) },
        actions = {
          IconButton(onClick = { eventSink(WeatherDetailsScreen.Event.Refresh) }) {
            Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.retry))
          }
        }
      )
    }
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
      if (details != null) {
        LazyColumn(
          modifier = Modifier.fillMaxSize()
        ) {
          item {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
              horizontalArrangement = Arrangement.End
            ) {
              Text(
                text = details.lastSyncedAt,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(8.dp)
              )
            }
          }
          item {
            CurrentSection(details.currentWeather, state.settings)
          }
          item {
            DetailsSection(details.currentWeather, state.settings)
          }
          item {
            ForecastSection(details.forecastWeather, state.settings)
          }
        }
      }

      if (state.isLoading && details == null) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
      }

      state.error?.let { error ->
        ErrorDialog(
          error = error,
          onDismiss = { eventSink(WeatherDetailsScreen.Event.DismissError) },
          onRetry = { eventSink(WeatherDetailsScreen.Event.Refresh) }
        )
      }
    }
  }
}

@Composable
private fun ErrorDialog(
  error: AppError,
  onDismiss: () -> Unit,
  onRetry: () -> Unit
) {
  val message = when (error) {
    AppError.NoInternet -> stringResource(R.string.error_no_internet)
    AppError.Timeout -> stringResource(R.string.error_timeout)
    AppError.EntityNotFound -> stringResource(R.string.error_not_found)
    is AppError.Unknown -> stringResource(R.string.error_unknown, error.message ?: "")
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(stringResource(R.string.app_name)) },
    text = { Text(message) },
    confirmButton = {
      TextButton(onClick = {
        onRetry()
        onDismiss()
      }) {
        Text(stringResource(R.string.retry))
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(stringResource(R.string.dismiss))
      }
    }
  )
}

@Composable
private fun ForecastSection(forecasts: List<ForecastSlot>, settings: UserSettings) {
  Column(
    Modifier
      .fillMaxWidth()
      .padding(16.dp)
  ) {
    Text(
      stringResource(R.string.hourly),
      style = MaterialTheme.typography.titleLarge
    )
    LazyRow {
      items(items = forecasts) { slot ->
        Card(
          modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp),
        ) {
          Column(
            modifier = Modifier
              .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(slot.hour)
            AsyncImage(
              modifier = Modifier.size(48.dp),
              model = slot.image,
              contentDescription = slot.condition?.description
            )
            Text(
              text = stringResource(
                id = R.string.temperature_format,
                slot.temperature,
                settings.temperatureUnit.symbol()
              ),
              modifier = Modifier.padding(8.dp),
            )
          }
        }
      }
    }
  }
}

@Composable
private fun CurrentSection(weather: CityWeather?, settings: UserSettings) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Column {
      Text(
        stringResource(
          id = R.string.temperature_format,
          weather?.temperature ?: 0.0,
          settings.temperatureUnit.symbol()
        ),
        style = MaterialTheme.typography.displayLarge
      )
      Text(
        stringResource(
          id = R.string.feels_like_format,
          weather?.feelsLike ?: 0.0,
          settings.temperatureUnit.symbol()
        ),
        style = MaterialTheme.typography.bodyMedium
      )
    }
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      AsyncImage(
        modifier = Modifier.size(60.dp),
        model = weather?.weatherIcon,
        contentDescription = weather?.weatherDescription
      )
      Text(
        text = weather?.weatherDescription.orEmpty(),
        style = MaterialTheme.typography.displaySmall
      )
    }
  }
}

@Composable
private fun DetailsSection(weather: CityWeather?, settings: UserSettings) {
  if (weather == null) return
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp)
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
      ) {
        DetailItem(stringResource(R.string.humidity, weather.humidity), Modifier.weight(1f))
        DetailItem(
          stringResource(
            R.string.pressure_format,
            weather.pressure,
            settings.pressureUnit.symbol()
          ),
          Modifier.weight(1f)
        )
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        DetailItem(
          stringResource(
            R.string.wind_speed_format,
            weather.windSpeed,
            settings.windSpeedUnit.symbol()
          ),
          Modifier.weight(1f)
        )
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = stringResource(R.string.wind_direction, weather.windDegrees),
            style = MaterialTheme.typography.bodyLarge
          )
          Spacer(Modifier.width(8.dp))
          Icon(
            painter = painterResource(id = R.drawable.ic_arrow_direction),
            contentDescription = null,
            modifier = Modifier
              .size(24.dp)
              .rotate(weather.windDegrees.toFloat())
          )
        }
      }
    }
  }
}

// TODO maybe move to common UI to be accessible for all screens
@Composable
private fun TemperatureUnit.symbol(): String = when (this) {
  TemperatureUnit.CELSIUS -> stringResource(R.string.unit_celsius)
  TemperatureUnit.FAHRENHEIT -> stringResource(R.string.unit_fahrenheit)
  TemperatureUnit.KELVIN -> stringResource(R.string.unit_kelvin)
}

@Composable
private fun WindSpeedUnit.symbol(): String = when (this) {
  WindSpeedUnit.METERS_PER_SECOND -> stringResource(R.string.unit_ms)
  WindSpeedUnit.KILOMETERS_PER_HOUR -> stringResource(R.string.unit_kmh)
  WindSpeedUnit.MILES_PER_HOUR -> stringResource(R.string.unit_mph)
  WindSpeedUnit.KNOTS -> stringResource(R.string.unit_kn)
  WindSpeedUnit.FEET_PER_SECOND -> stringResource(R.string.unit_fts)
}

@Composable
private fun PressureUnit.symbol(): String = when (this) {
  PressureUnit.HECTOPASCAL -> stringResource(R.string.unit_hpa)
  PressureUnit.KILOPASCAL -> stringResource(R.string.unit_kpa)
  PressureUnit.MILLIBAR -> stringResource(R.string.unit_mbar)
  PressureUnit.ATMOSPHERE -> stringResource(R.string.unit_atm)
  PressureUnit.MILLIMETERS_OF_MERCURY -> stringResource(R.string.unit_mmhg)
  PressureUnit.INCHES_OF_MERCURY -> stringResource(R.string.unit_inhg)
}

@Composable
private fun DetailItem(text: String, modifier: Modifier = Modifier) {
  Text(
    text = text,
    style = MaterialTheme.typography.bodyLarge,
    modifier = modifier
  )
}
