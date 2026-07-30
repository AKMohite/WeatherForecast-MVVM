package app.mak.atmosense.feature.cities

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
  Text(state.dummy)
}
