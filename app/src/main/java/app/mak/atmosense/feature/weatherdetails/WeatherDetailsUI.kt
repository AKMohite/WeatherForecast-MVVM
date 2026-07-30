package app.mak.atmosense.feature.weatherdetails

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@CircuitInject(screen = WeatherDetailsScreen::class, scope = AppScope::class)
@Composable
internal fun WeatherDetailsUI(modifier: Modifier = Modifier) {
  Text("Weather Details Screen")
}
