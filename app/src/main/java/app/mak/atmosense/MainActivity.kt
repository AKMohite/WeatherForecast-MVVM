package app.mak.atmosense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.mak.atmosense.feature.cities.CityManagementScreen
import app.mak.atmosense.ui.theme.AtmosenseTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AtmosenseTheme {
        CityManagementScreen()
      }
    }
  }
}
