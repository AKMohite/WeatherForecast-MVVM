package app.mak.atmosense.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import app.mak.atmosense.core.common.model.UserSettings
import app.mak.atmosense.core.domain.usecase.ObserveUserSettings
import app.mak.atmosense.core.domain.usecase.UpdateUserSettings
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.launch

@AssistedInject
class SettingsPresenter(
  @Assisted private val navigator: Navigator,
  private val observeUserSettings: ObserveUserSettings,
  private val updateUserSettings: UpdateUserSettings
) : Presenter<SettingsScreen.State> {

  @Composable
  override fun present(): SettingsScreen.State {
    val settings by observeUserSettings().collectAsState(initial = UserSettings())
    val scope = rememberCoroutineScope()

    return SettingsScreen.State(settings) { event ->
      when (event) {
        is SettingsScreen.Event.UpdateSettings -> {
          scope.launch { updateUserSettings(event.settings) }
        }

        SettingsScreen.Event.Back -> navigator.pop()
      }
    }
  }

  @CircuitInject(SettingsScreen::class, AppScope::class)
  @AssistedFactory
  interface Factory {
    fun create(navigator: Navigator): SettingsPresenter
  }
}
