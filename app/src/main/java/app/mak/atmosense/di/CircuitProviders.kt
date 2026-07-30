package app.mak.atmosense.di

import androidx.compose.foundation.background
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.LocalCircuit
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface CircuitProviders {

//  @Provides
//  fun provideCircuit(): Circuit {
//    return Circuit.Builder()
//        .addPresenter<CityManagementScreen, CityManagementScreen.State>(
//          CityManagementPresenter()
//        )
//        .addUi<CityManagementScreen, CityManagementScreen.State> { state, modifier ->
//          CityManagementUI(
//            state,
//            modifier
//          )
//        }
//        .build()
//  }

  @Multibinds
  fun presenterFactories(): Set<Presenter.Factory>

  @Multibinds
  fun viewFactories(): Set<Ui.Factory>

  @SingleIn(AppScope::class)
  @Provides
  fun provideCircuit(
    presenterFactories: Set<Presenter.Factory>,
    uiFactories: Set<Ui.Factory>
  ): Circuit {
    return Circuit.Builder()
      .addPresenterFactories(presenterFactories)
      .addUiFactories(uiFactories)
      .setOnUnavailableContent { screen, modifier ->
        val circuit = LocalCircuit.current
        BasicText(
          """
              Route not available: ${screen.javaClass.name}.
              Presenter: ${circuit?.presenter(screen, Navigator.NoOp)?.javaClass}
              UI: ${circuit?.ui(screen)?.javaClass}
              All presenterFactories: ${circuit?.newBuilder()?.presenterFactories}
              All uiFactories: ${circuit?.newBuilder()?.uiFactories}
              """
            .trimIndent(),
          modifier.background(Color.Red),
          style = TextStyle(color = Color.Yellow),
        )
      }
      .build()
  }
}
