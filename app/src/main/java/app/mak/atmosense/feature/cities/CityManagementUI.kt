package app.mak.atmosense.feature.cities

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import app.mak.atmosense.R
import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.TemperatureUnit
import app.mak.atmosense.core.common.model.UserSettings
import app.mak.atmosense.ui.theme.AtmosenseTheme
import coil3.compose.AsyncImage
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(screen = CityManagementScreen::class, scope = AppScope::class)
@Composable
internal fun CityManagementUI(
  state: CityManagementScreen.State,
  modifier: Modifier = Modifier
) {
  val snackbarHostState = SnackbarHostState()
  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        actions = {
          val eventSink = when (state) {
            is CityManagementScreen.State.Empty -> state.eventSink
            is CityManagementScreen.State.Success -> state.eventSink
            else -> null
          }
          if (eventSink != null) {
            IconButton(onClick = { eventSink(CityManagementScreen.Event.OpenSettings) }) {
              Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
            }
          }
        }
      )
    },
    snackbarHost = { SnackbarHost(snackbarHostState) },
    floatingActionButton = {
      if (state is CityManagementScreen.State.Success) {
        FloatingActionButton(
          onClick = { state.eventSink(CityManagementScreen.Event.SearchLocation) },
        ) {
          Text(
            modifier = Modifier
              .padding(horizontal = 12.dp),
            text = stringResource(R.string.add_location)
          )
        }
      }
    }
  ) { paddingValues ->
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(vertical = paddingValues.calculateTopPadding()),
    ) {
      when (state) {
        is CityManagementScreen.State.Empty -> EmptyContent(
          event = state.eventSink,
          snackbarHostState = snackbarHostState
        )

        is CityManagementScreen.State.Error -> {
          Text(text = state.message)
        }

        CityManagementScreen.State.Loading -> CircularProgressIndicator(
          modifier = Modifier.align(Alignment.Center)
        )

        is CityManagementScreen.State.Success -> WeatherForCitiesContent(
          cities = state.cities,
          settings = state.settings,
          onCityClick = { id ->
            state.eventSink(CityManagementScreen.Event.Details(id))
          }
        )
      }
    }
  }
}

@Composable
private fun WeatherForCitiesContent(
  cities: List<CityWeather>,
  settings: UserSettings,
  onCityClick: (Long) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize(),
    contentPadding = PaddingValues(8.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    items(items = cities, key = { city -> city.cityId }) { city ->
      WeatherForCityUI(
        city = city,
        settings = settings,
        onCityClick = onCityClick
      )
    }
  }
}

@Composable
fun WeatherForCityUI(
  city: CityWeather,
  settings: UserSettings,
  modifier: Modifier = Modifier,
  onCityClick: (Long) -> Unit
) {
  Card(
    modifier = modifier
      .fillMaxWidth(),
    onClick = { onCityClick(city.cityId) }
  ) {
    Row(
      modifier = modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      AsyncImage(
        modifier = Modifier
          .size(48.dp),
        model = city.weatherIcon,
        contentDescription = city.weatherDescription
      )
      Column {
        Text(
          text = city.cityName,
          style = MaterialTheme.typography.bodyLarge
        )
        Text(
          text = city.weatherDescription,
          style = MaterialTheme.typography.bodySmall
        )
      }
      Spacer(Modifier.weight(1f))
      Text(
        text = stringResource(
          id = R.string.temperature_format,
          city.temperature,
          settings.temperatureUnit.symbol()
        ),
        style = MaterialTheme.typography.titleMedium
      )

    }
  }
}

// TODO we can have units mapped to domain model instead of passing settings
@Composable
private fun TemperatureUnit.symbol(): String = when (this) {
  TemperatureUnit.CELSIUS -> stringResource(R.string.unit_celsius)
  TemperatureUnit.FAHRENHEIT -> stringResource(R.string.unit_fahrenheit)
  TemperatureUnit.KELVIN -> stringResource(R.string.unit_kelvin)
}

private val locationPermissions = arrayOf(
  Manifest.permission.ACCESS_FINE_LOCATION,
  Manifest.permission.ACCESS_COARSE_LOCATION
)

@Composable
private fun EmptyContent(
  event: (CityManagementScreen.Event) -> Unit,
  snackbarHostState: SnackbarHostState
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val permissionDeniedMessage = stringResource(R.string.location_permission_denied)
  val settingsActionLabel = stringResource(R.string.settings)

  val locationPermissionResultLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions(),
  ) { result ->
    val isPermissionGranted = locationPermissions.any { permission -> result[permission] == true }
    if (isPermissionGranted) {
      event(CityManagementScreen.Event.FetchCurrentLocationWeather)
    } else {
      val shouldShowRationale = locationPermissions.any { permission ->
        ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)
      }

      scope.launch {
        val snackbarResult = snackbarHostState.showSnackbar(
          message = permissionDeniedMessage,
          actionLabel = if (!shouldShowRationale) settingsActionLabel else null,
          duration = SnackbarDuration.Short
        )
        if (snackbarResult == SnackbarResult.ActionPerformed) {
          event(CityManagementScreen.Event.OpenAppSettings)
        }
      }
    }
  }
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = stringResource(R.string.no_cities_found),
      style = MaterialTheme.typography.titleLarge
    )
    Spacer(Modifier.height(8.dp))
    Text(
      text = stringResource(R.string.no_cities_found_description),
      style = MaterialTheme.typography.bodyMedium
    )
    Spacer(Modifier.height(16.dp))
    Button(
      modifier = Modifier.fillMaxWidth(),
      onClick = { event(CityManagementScreen.Event.SearchLocation) }
    ) {
      Text(text = stringResource(R.string.search_new_location))
    }
    Spacer(Modifier.height(8.dp))
    OutlinedButton(
      modifier = Modifier.fillMaxWidth(),
      onClick = {
        locationPermissionResultLauncher.launch(locationPermissions)
      }
    ) {
      Icon(
        painter = painterResource(id = R.drawable.img_location),
        contentDescription = stringResource(R.string.fetch_current_location),
        modifier = Modifier.size(18.dp)
      )
      Spacer(Modifier.width(8.dp))
      Text(text = stringResource(R.string.get_current_location))
    }
  }
}


private class CityManagementStateParameterProvider :
  PreviewParameterProvider<CityManagementScreen.State> {
  override val values: Sequence<CityManagementScreen.State> = sequenceOf(
    CityManagementScreen.State.Loading,
    CityManagementScreen.State.Empty({}),
    CityManagementScreen.State.Success(
      cities = listOf(
        CityWeather(
          cityId = 1,
          cityName = "New York",
          countryCode = "US",
          temperature = 25.0,
          feelsLike = 27.0,
          weatherIcon = "app:://atmosense.com/img/01d@2x.png",
          weatherDescription = "Clear sky",
          fetchedBefore = "10 mins ago",
          humidity = 10L,
          pressure = 10.0,
          windSpeed = 10.0,
          windDegrees = 10L
        ),
        CityWeather(
          cityId = 2,
          cityName = "London",
          countryCode = "GB",
          temperature = 18.0,
          feelsLike = 17.0,
          weatherIcon = "app:://atmosense.com/img/09d@2x.png",
          weatherDescription = "Light rain",
          fetchedBefore = "20 mins ago",
          humidity = 10L,
          pressure = 10.0,
          windSpeed = 10.0,
          windDegrees = 10L
        ),
        CityWeather(
          cityId = 3,
          cityName = "Tokyo",
          countryCode = "JP",
          temperature = 30.0,
          feelsLike = 35.0,
          weatherIcon = "app:://atmosense.com/img/11d@2x.png",
          weatherDescription = "Thunderstorm",
          fetchedBefore = "5 mins ago",
          humidity = 10L,
          pressure = 10.0,
          windSpeed = 10.0,
          windDegrees = 10L
        )
      ),
      eventSink = {}
    ),
    CityManagementScreen.State.Error(message = "Failed to load weather data. Please try again.")
  )

  override fun getDisplayName(index: Int): String? {
    return super.getDisplayName(index)
  }
}

@Preview(showBackground = true)
@Composable
private fun CityManagementUIPreview(
  @PreviewParameter(CityManagementStateParameterProvider::class) state: CityManagementScreen.State
) {
  AtmosenseTheme {
    CityManagementUI(state = state)
  }
}
