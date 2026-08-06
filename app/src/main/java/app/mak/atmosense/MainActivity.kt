package app.mak.atmosense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.mak.atmosense.feature.cities.CityManagementScreen
import app.mak.atmosense.ui.theme.AtmosenseTheme
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator

class MainActivity : ComponentActivity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    // TODO need to check graph
    val appGraph =
      (application as? AtmosenseApp)?.appGraph ?: error("AppGraph not found for $application")
    setContent {
      AtmosenseTheme {
        val backStack = rememberSaveableBackStack(CityManagementScreen)
        val navigator = rememberCircuitNavigator(backStack)
        CircuitCompositionLocals(appGraph.circuit) {
          NavigableCircuitContent(navigator = navigator, backStack = backStack)
        }
      }
    }
  }
}
