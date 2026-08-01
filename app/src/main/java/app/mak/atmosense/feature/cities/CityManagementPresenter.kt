package app.mak.atmosense.feature.cities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import app.mak.atmosense.core.domain.usecase.ObserveCitiesWeather
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
  private val fetchCurrentLocationWeather: FetchCurrentLocationWeather,
  private val observeCitiesWeather: ObserveCitiesWeather
) : Presenter<CityManagementScreen.State> {

  @Composable
  override fun present(): CityManagementScreen.State {
    val cities: String? = "emptyList<String>()"
    val scope = rememberCoroutineScope()
    val eventSink: (CityManagementScreen.Event) -> Unit = { event ->
      when (event) {
        CityManagementScreen.Event.SearchLocation -> navigator.goTo(SearchScreen)
        is CityManagementScreen.Event.Details -> navigator.goTo(WeatherDetailsScreen(event.cityId))
        CityManagementScreen.Event.FetchCurrentLocationWeather -> {
          scope.launch {
            fetchCurrentLocationWeather()
          }
        }

        CityManagementScreen.Event.OpenAppSettings -> {}
      }
    }
    return when {
      cities == null -> CityManagementScreen.State.Loading
      cities.isEmpty() -> CityManagementScreen.State.Empty
      else -> CityManagementScreen.State.Success(cities, eventSink)
    }
  }

  @CircuitInject(CityManagementScreen::class, AppScope::class)
  @AssistedFactory
  interface Factory {
    fun create(navigator: Navigator): CityManagementPresenter
  }
}
