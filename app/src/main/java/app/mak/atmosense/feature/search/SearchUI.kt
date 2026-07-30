package app.mak.atmosense.feature.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@CircuitInject(screen = SearchScreen::class, scope = AppScope::class)
@Composable
internal fun SearchUI(modifier: Modifier = Modifier) {
  Text("Search Screen")
}
