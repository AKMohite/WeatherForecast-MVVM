package app.mak.atmosense.feature.cities

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import app.mak.atmosense.R
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import kotlinx.coroutines.launch

@CircuitInject(screen = CityManagementScreen::class, scope = AppScope::class)
@Composable
internal fun CityManagementUI(
  state: CityManagementScreen.State,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Text(state.dummy)
    Button(onClick = { state.eventSink(CityManagementScreen.Event.SearchLocation) }) { Text("Search") }
    Button(onClick = { state.eventSink(CityManagementScreen.Event.Details(4586)) }) { Text("Details") }
  }
}

private val locationPermissions = arrayOf(
  Manifest.permission.ACCESS_FINE_LOCATION,
  Manifest.permission.ACCESS_COARSE_LOCATION
)

@Composable
private fun EmptyContent(
  event: (CityManagementScreen.Event) -> Unit,
  snackbarHostState: SnackbarHostState
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val permissionDeniedMessage = stringResource(R.string.location_permission_denied)
  val settingsActionLabel = stringResource(R.string.settings)

  val locationPermissionResultLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions(),
  ) { result ->
    val isPermissionGranted = locationPermissions.any { permission -> result[permission] == true }
    if (isPermissionGranted) {
      event(CityManagementScreen.Event.FetchCurrentLocationWeather)
    } else {
      val shouldShowRationale = locationPermissions.any { permission ->
        ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)
      }

      scope.launch {
        val snackbarResult = snackbarHostState.showSnackbar(
          message = permissionDeniedMessage,
          actionLabel = if (!shouldShowRationale) settingsActionLabel else null,
          duration = SnackbarDuration.Short
        )
        if (snackbarResult == SnackbarResult.ActionPerformed) {
          event(CityManagementScreen.Event.OpenAppSettings)
        }
      }
    }
  }
  Column(
    modifier = Modifier
      .padding(horizontal = 12.dp)
  ) {
    Button(
      modifier = Modifier.fillMaxWidth(),
      onClick = { event(CityManagementScreen.Event.SearchLocation) }
    ) {
      Text(text = stringResource(R.string.search_new_location))
    }
    Spacer(Modifier.height(8.dp))
    OutlinedButton(
      modifier = Modifier.fillMaxWidth(),
      onClick = {
        locationPermissionResultLauncher.launch(locationPermissions)
      }
    ) {
      Icon(
        painter = painterResource(id = R.drawable.img_location),
        contentDescription = stringResource(R.string.fetch_current_location),
        modifier = Modifier.size(18.dp)
      )
      Spacer(Modifier.width(8.dp))
      Text(text = stringResource(R.string.get_current_location))
    }
  }
}

