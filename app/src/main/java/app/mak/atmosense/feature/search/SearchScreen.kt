package app.mak.atmosense.feature.search

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object SearchScreen : Screen {
  data class State(
    val query: String
  ) : CircuitUiState
}
