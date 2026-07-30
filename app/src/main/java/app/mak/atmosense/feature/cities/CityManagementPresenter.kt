package app.mak.atmosense.feature.cities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.slack.circuit.runtime.presenter.Presenter

internal class CityManagementPresenter(
//  private val weatherRepository: WeatherRepository
) : Presenter<CityManagementScreen.State> {

  @Composable
  override fun present(): CityManagementScreen.State {
    val cities by produceState("Empty String") {
//      value = weatherRepository.getCurrentWeather()
      value = "new string"
    }
    return CityManagementScreen.State(
      dummy = cities
    )
  }
}
