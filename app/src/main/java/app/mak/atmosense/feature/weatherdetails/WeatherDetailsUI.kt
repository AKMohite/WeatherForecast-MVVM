package app.mak.atmosense.feature.weatherdetails

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.mak.atmosense.R
import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.ForecastSlot
import coil3.compose.AsyncImage
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@CircuitInject(screen = WeatherDetailsScreen::class, scope = AppScope::class)
@Composable
internal fun WeatherDetailsUI(
  modifier: Modifier = Modifier,
  state: WeatherDetailsScreen.State
) {
  val details = state.details ?: return
  Scaffold(
    modifier = modifier
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(it)
    ) {
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = details.cityName,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
          )
          Text(
            text = details.lastSyncedAt,
            style = MaterialTheme.typography.bodySmall,
          )
        }
      }
      item {
        CurrentSection(details.currentWeather)
      }
      item {
        DetailsSection(details.currentWeather)
      }
      item {
        ForecastSection(details.forecastWeather)
      }
    }
  }
}

@Composable
private fun ForecastSection(forecasts: List<ForecastSlot>) {
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
              text = stringResource(id = R.string.temperature, slot.temperature),
              modifier = Modifier.padding(8.dp),
            )
          }
        }
      }
    }
  }
}

@Composable
private fun CurrentSection(weather: CityWeather?) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Column {
      Text(
        stringResource(id = R.string.temperature, weather?.temperature ?: 0.0),
        style = MaterialTheme.typography.displayLarge
      )
      Text(
        stringResource(id = R.string.feels_like, weather?.feelsLike ?: 0.0),
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
private fun DetailsSection(weather: CityWeather?) {
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
        DetailItem(stringResource(R.string.pressure, weather.pressure), Modifier.weight(1f))
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        DetailItem(stringResource(R.string.wind_speed, weather.windSpeed), Modifier.weight(1f))
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

@Composable
private fun DetailItem(text: String, modifier: Modifier = Modifier) {
  Text(
    text = text,
    style = MaterialTheme.typography.bodyLarge,
    modifier = modifier
  )
}
