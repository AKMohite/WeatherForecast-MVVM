package app.mak.atmosense.feature.cities

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object CityManagementScreen : Screen {

  sealed interface State : CircuitUiState {
    data object Loading : State
    data object Empty : State
    data class Success(
      val dummy: String,
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
