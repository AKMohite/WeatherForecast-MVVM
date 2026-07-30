package app.mak.atmosense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.mak.atmosense.feature.cities.CityManagementPresenter
import app.mak.atmosense.feature.cities.CityManagementScreen
import app.mak.atmosense.feature.cities.CityManagementUI
import app.mak.atmosense.ui.theme.AtmosenseTheme
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent

class MainActivity : ComponentActivity() {

  val circuit: Circuit =
    Circuit.Builder()
      .addPresenter<CityManagementScreen, CityManagementScreen.State>(
        CityManagementPresenter()
      )
      .addUi<CityManagementScreen, CityManagementScreen.State> { state, modifier ->
        CityManagementUI(
          state,
          modifier
        )
      }
      .build()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AtmosenseTheme {
        CircuitCompositionLocals(circuit = circuit) {
          CircuitContent(CityManagementScreen)
        }
      }
    }
  }
}
