package app.mak.atmosense.feature.cities

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

@AssistedInject
class CityManagementPresenter(
  @Assisted private val navigator: Navigator
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

  @CircuitInject(CityManagementScreen::class, AppScope::class)
  @AssistedFactory
  interface Factory {
    fun create(navigator: Navigator): CityManagementPresenter
  }
}
