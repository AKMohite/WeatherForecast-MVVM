package app.mak.atmosense.feature.cities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import app.mak.atmosense.core.domain.repository.WeatherRepository
import app.mak.atmosense.core.location.LocationAccessCoordinator
import app.mak.atmosense.feature.search.SearchScreen
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
  private val weatherRepository: WeatherRepository,
  private val locationAccessCoordinator: LocationAccessCoordinator,
) : Presenter<CityManagementScreen.State> {

  @Composable
  override fun present(): CityManagementScreen.State {
    val scope = rememberCoroutineScope()
    val cities by produceState("Empty String") {
      value = weatherRepository.getCurrentWeather()
    }
    return CityManagementScreen.State(
      dummy = cities,
      eventSink = { event ->
        when (event) {
          CityManagementScreen.Event.SearchLocation -> navigator.goTo(SearchScreen)
          is CityManagementScreen.Event.Details -> navigator.goTo(WeatherDetailsScreen(event.cityId))
          CityManagementScreen.Event.FetchCurrentLocationWeather -> {
            scope.launch {
              val result = locationAccessCoordinator.resolveCurrentLocation()
              println(result)
            }
          }

          CityManagementScreen.Event.OpenAppSettings -> {}
        }
      }
    )
  }

  @CircuitInject(CityManagementScreen::class, AppScope::class)
  @AssistedFactory
  interface Factory {
    fun create(navigator: Navigator): CityManagementPresenter
  }
}
