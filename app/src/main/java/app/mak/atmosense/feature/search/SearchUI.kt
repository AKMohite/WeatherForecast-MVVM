package app.mak.atmosense.feature.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import app.mak.atmosense.R
import app.mak.atmosense.core.common.model.SearchCity
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@CircuitInject(screen = SearchScreen::class, scope = AppScope::class)
@Composable
internal fun SearchUI(
  state: SearchScreen.State,
  modifier: Modifier = Modifier
) {
  var query by rememberSaveable { mutableStateOf("") }
  val eventSink = state.eventSink
  Box(
    modifier = modifier
      .fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    AnimatedVisibility(state.isLoading) {
      CircularProgressIndicator(
        modifier = Modifier.padding(16.dp)
      )
    }
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 8.dp)
    ) {
      OutlinedTextField(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp),
        value = query,
        placeholder = { Text(text = stringResource(R.string.search_hint)) },
        onValueChange = {
          query = it
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { eventSink(SearchScreen.Event.Search(query)) }),
        singleLine = true,
        maxLines = 1
      )

      LazyColumn {
        items(items = state.cities, key = { result -> result.id }) { result ->
          CityResult(
            result = result,
            onCityClick = {
              eventSink(SearchScreen.Event.OnCitySelected(result))
            }
          )
        }
      }
    }
  }
}

@Composable
private fun CityResult(
  result: SearchCity,
  modifier: Modifier = Modifier,
  onCityClick: (SearchCity) -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(8.dp)
      .clickable {
        onCityClick(result)
      }
  ) {
    Text(
      text = result.name,
      style = MaterialTheme.typography.bodyLarge
    )
    Text(
      text = result.subtitle,
      style = MaterialTheme.typography.bodySmall
    )
  }
}
