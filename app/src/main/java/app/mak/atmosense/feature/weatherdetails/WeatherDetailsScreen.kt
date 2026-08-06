package app.mak.atmosense.feature.weatherdetails

import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.WeatherDetails
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherDetailsScreen(
  val cityId: Long
) : Screen {

  data class State(
    val details: WeatherDetails?,
    val isLoading: Boolean = false,
    val error: AppError? = null,
    val eventSink: (Event) -> Unit = {}
  ) : CircuitUiState

  sealed interface Event : CircuitUiEvent {
    data object Refresh : Event
    data object DismissError : Event
  }
}
