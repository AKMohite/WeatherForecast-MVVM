package app.mak.atmosense.feature.weatherdetails

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

@AssistedInject
class WeatherDetailsPresenter(
  @Assisted private val navigator: Navigator
) : Presenter<WeatherDetailsScreen.State> {
  @Composable
  override fun present(): WeatherDetailsScreen.State {
    return WeatherDetailsScreen.State(45545)
  }

  @CircuitInject(WeatherDetailsScreen::class, AppScope::class)
  @AssistedFactory
  interface Factory {
    fun create(navigator: Navigator): WeatherDetailsPresenter
  }
}
