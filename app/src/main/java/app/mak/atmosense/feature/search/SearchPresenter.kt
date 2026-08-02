package app.mak.atmosense.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.SearchCity
import app.mak.atmosense.core.domain.repository.WeatherRepository
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.launch

@AssistedInject
class SearchPresenter(
  @Assisted private val navigator: Navigator,
  private val weatherRepository: WeatherRepository
) : Presenter<SearchScreen.State> {
  @Composable
  override fun present(): SearchScreen.State {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var cities by remember { mutableStateOf(emptyList<SearchCity>()) }
    val eventSink: (SearchScreen.Event) -> Unit = { event ->
      when (event) {
        is SearchScreen.Event.Search -> {
          scope.launch {
            isLoading = true
            when (val citiesResult = weatherRepository.searchCities(event.query)) {
              is AppResult.Failure -> {
                error = citiesResult.exception?.message
              }

              is AppResult.Success -> {
                cities = citiesResult.value
              }
            }
          }.invokeOnCompletion {
            isLoading = false
          }
        }

        is SearchScreen.Event.OnCitySelected -> {
          scope.launch {
            when (val result = weatherRepository.fetchCurrentWeather(event.city.toCoordinates())) {
              is AppResult.Failure -> {
                error = result.exception?.message
              }

              is AppResult.Success -> {
                navigator.pop()
              }
            }
          }
        }
      }
    }
    return SearchScreen.State(
      isLoading = isLoading,
      cities = cities,
      error = error,
      eventSink = eventSink
    )
  }

  @CircuitInject(SearchScreen::class, AppScope::class)
  @AssistedFactory
  interface Factory {
    fun create(navigator: Navigator): SearchPresenter
  }
}
