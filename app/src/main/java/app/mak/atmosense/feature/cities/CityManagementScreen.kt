package app.mak.atmosense.feature.cities

import app.mak.atmosense.core.common.model.CityWeather
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object CityManagementScreen : Screen {

  sealed interface State : CircuitUiState {
    data object Loading : State
    data class Empty(
      val eventSink: (Event) -> Unit
    ) : State
    data class Success(
      val cities: List<CityWeather>,
      val eventSink: (Event) -> Unit
    ) : State

    data class Error(val message: String) : State
  }

  sealed interface Event : CircuitUiEvent {
    data object SearchLocation : Event
    data object FetchCurrentLocationWeather : Event
    data object OpenAppSettings : Event
    data class Details(val cityId: Long) : Event
  }

}
