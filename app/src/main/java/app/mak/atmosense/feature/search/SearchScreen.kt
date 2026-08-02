package app.mak.atmosense.feature.search

import app.mak.atmosense.core.common.model.SearchCity
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object SearchScreen : Screen {
  data class State(
    val isLoading: Boolean,
    val error: String?,
    val cities: List<SearchCity>,
    val eventSink: (Event) -> Unit
  ) : CircuitUiState

  sealed interface Event {
    data class Search(val query: String) : Event
    data class OnCitySelected(val city: SearchCity) : Event
  }
}


