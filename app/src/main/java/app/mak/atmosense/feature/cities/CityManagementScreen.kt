package app.mak.atmosense.feature.cities

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object CityManagementScreen : Screen {

  data class State(
    val dummy: String
  ) : CircuitUiState

}
