package app.mak.atmosense.feature.cities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.UserSettings
import app.mak.atmosense.core.domain.usecase.ObserveCitiesWeather
import app.mak.atmosense.core.domain.usecase.ObserveUserSettings
import app.mak.atmosense.feature.search.SearchScreen
import app.mak.atmosense.feature.settings.SettingsScreen
import app.mak.atmosense.feature.weatherdetails.WeatherDetailsScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.launch

@AssistedInject
class CityManagementPresenter(
  @Assisted private val navigator: Navigator,
  private val fetchCurrentLocationWeather: FetchCurrentLocationWeather,
  private val observeCitiesWeather: ObserveCitiesWeather,
  private val observeUserSettings: ObserveUserSettings
) : Presenter<CityManagementScreen.State> {

  @Composable
  override fun present(): CityManagementScreen.State {
    val weatherForCities by produceState<List<CityWeather>?>(null) {
      observeCitiesWeather().collect { cities ->
        value = cities
      }
    }
    val settings by observeUserSettings().collectAsState(initial = UserSettings())
    val scope = rememberCoroutineScope()
    return when {
      weatherForCities == null -> CityManagementScreen.State.Loading
      weatherForCities.isNullOrEmpty() -> CityManagementScreen.State.Empty(
        eventSink = { event ->
          when (event) {
            CityManagementScreen.Event.SearchLocation -> navigator.goTo(SearchScreen)
            CityManagementScreen.Event.FetchCurrentLocationWeather -> {
              scope.launch {
                fetchCurrentLocationWeather()
              }
            }
            CityManagementScreen.Event.OpenSettings -> navigator.goTo(SettingsScreen)

            else -> {}
          }
        }
      )
      else -> CityManagementScreen.State.Success(
        cities = weatherForCities ?: error("Invalid state cities are null: $weatherForCities"),
        settings = settings,
        eventSink = { event ->
          when (event) {
            CityManagementScreen.Event.SearchLocation -> navigator.goTo(SearchScreen)
            is CityManagementScreen.Event.Details -> navigator.goTo(WeatherDetailsScreen(event.cityId))
            CityManagementScreen.Event.FetchCurrentLocationWeather -> {
              scope.launch {
                fetchCurrentLocationWeather()
              }
            }

            CityManagementScreen.Event.OpenAppSettings -> {}
            CityManagementScreen.Event.OpenSettings -> navigator.goTo(SettingsScreen)
          }
        }
      )
    }
  }

  @CircuitInject(CityManagementScreen::class, AppScope::class)
  @AssistedFactory
  interface Factory {
    fun create(navigator: Navigator): CityManagementPresenter
  }
}
