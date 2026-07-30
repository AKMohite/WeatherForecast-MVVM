package app.mak.atmosense.feature.cities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@CircuitInject(screen = CityManagementScreen::class, scope = AppScope::class)
@Composable
internal fun CityManagementUI(
  state: CityManagementScreen.State,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Text(state.dummy)
    Button(onClick = { state.eventSink(CityManagementScreen.Event.Search) }) { Text("Search") }
    Button(onClick = { state.eventSink(CityManagementScreen.Event.Details(4586)) }) { Text("Details") }
  }
}
