package app.mak.atmosense.feature.search

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

@AssistedInject
class SearchPresenter(
  @Assisted private val navigator: Navigator
) : Presenter<SearchScreen.State> {
  @Composable
  override fun present(): SearchScreen.State {
    return SearchScreen.State("Search Screen")
  }

  @CircuitInject(SearchScreen::class, AppScope::class)
  @AssistedFactory
  interface Factory {
    fun create(navigator: Navigator): SearchPresenter
  }
}
