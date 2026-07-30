package app.mak.atmosense.feature.cities

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun CityManagementUI(
  state: CityManagementScreen.State,
  modifier: Modifier = Modifier
) {
  Text(state.dummy)
}
