package app.mak.atmosense.feature.settings

import app.mak.atmosense.core.common.model.UserSettings
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
object SettingsScreen : Screen {
  data class State(
    val settings: UserSettings,
    val eventSink: (Event) -> Unit
  ) : CircuitUiState

  sealed interface Event : CircuitUiEvent {
    data class UpdateSettings(val settings: UserSettings) : Event
    data object Back : Event
  }
}
