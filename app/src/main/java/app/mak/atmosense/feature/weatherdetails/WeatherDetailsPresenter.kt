package app.mak.atmosense.feature.weatherdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.mak.atmosense.core.domain.usecase.ObserveWeatherDetails
import app.mak.atmosense.core.domain.usecase.RefreshWeatherDetails
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

@AssistedInject
class WeatherDetailsPresenter(
  @Assisted private val screen: WeatherDetailsScreen,
  @Assisted private val navigator: Navigator,
  private val refreshWeatherDetails: RefreshWeatherDetails,
  private val observeWeatherDetails: ObserveWeatherDetails
) : Presenter<WeatherDetailsScreen.State> {

  @Composable
  override fun present(): WeatherDetailsScreen.State {
    val details by observeWeatherDetails(screen.cityId).collectAsStateWithLifecycle(initialValue = null)
    LaunchedEffect(Unit) {
      refreshWeatherDetails(cityId = screen.cityId)
    }
    return WeatherDetailsScreen.State(
      details = details
    )
  }

  @CircuitInject(WeatherDetailsScreen::class, AppScope::class)
  @AssistedFactory
  interface Factory {
    fun create(
      screen: WeatherDetailsScreen,
      navigator: Navigator
    ): WeatherDetailsPresenter
  }
}
