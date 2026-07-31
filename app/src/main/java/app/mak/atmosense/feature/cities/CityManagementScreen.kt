package app.mak.atmosense.feature.cities

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object CityManagementScreen : Screen {

  data class State(
    val dummy: String,
    val eventSink: (Event) -> Unit,
  ) : CircuitUiState

  sealed interface Event : CircuitUiEvent {
    data object SearchLocation : Event
    data object FetchCurrentLocationWeather : Event
    data object OpenAppSettings : Event
    data class Details(val cityId: Long) : Event
  }

}
