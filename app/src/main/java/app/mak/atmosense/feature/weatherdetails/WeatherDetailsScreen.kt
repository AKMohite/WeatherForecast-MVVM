package app.mak.atmosense.feature.weatherdetails

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherDetailsScreen(
  val cityId: Long
) : Screen {
}
