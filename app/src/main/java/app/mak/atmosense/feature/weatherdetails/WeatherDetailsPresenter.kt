package app.mak.atmosense.feature.weatherdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.UserSettings
import app.mak.atmosense.core.domain.usecase.ObserveUserSettings
import app.mak.atmosense.core.domain.usecase.ObserveWeatherDetails
import app.mak.atmosense.core.domain.usecase.RefreshWeatherDetails
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.launch

@AssistedInject
class WeatherDetailsPresenter(
  @Assisted private val screen: WeatherDetailsScreen,
  @Assisted private val navigator: Navigator,
  private val refreshWeatherDetails: RefreshWeatherDetails,
  private val observeWeatherDetails: ObserveWeatherDetails,
  private val observeUserSettings: ObserveUserSettings
) : Presenter<WeatherDetailsScreen.State> {

  @Composable
  override fun present(): WeatherDetailsScreen.State {
    val details by observeWeatherDetails(screen.cityId).collectAsStateWithLifecycle(initialValue = null)
    val settings by observeUserSettings().collectAsStateWithLifecycle(initialValue = UserSettings())
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<AppError?>(null) }
    val scope = rememberCoroutineScope()

    suspend fun refresh(isForce: Boolean = false) {
      isLoading = true
      error = null
      val result = refreshWeatherDetails(isForceRefresh = isForce, cityId = screen.cityId)
      if (result is AppResult.Failure) {
        error = result.error
      }
      isLoading = false
    }

    LaunchedEffect(Unit) {
      refresh()
    }

    return WeatherDetailsScreen.State(
      details = details,
      settings = settings,
      isLoading = isLoading,
      error = error
    ) { event ->
      when (event) {
        WeatherDetailsScreen.Event.Refresh -> scope.launch { refresh(isForce = true) }
        WeatherDetailsScreen.Event.DismissError -> error = null
      }
    }
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
