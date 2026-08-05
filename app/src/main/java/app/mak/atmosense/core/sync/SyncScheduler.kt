package app.mak.atmosense.core.sync

import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dev.zacsweers.metro.Inject
import java.util.concurrent.TimeUnit

@Inject
class SyncScheduler(
  private val workManager: WorkManager,
) {
  fun schedulePeriodicSync() {
    Log.d("SyncScheduler", "Scheduling periodic sync...")
    val request = PeriodicWorkRequestBuilder<WeatherSyncWorker>(
      repeatInterval = 30,
      repeatIntervalTimeUnit = TimeUnit.MINUTES,
//            flexTimeInterval = 15,
//            flexTimeIntervalUnit = TimeUnit.MINUTES,
    ).setConstraints(
      Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build(),
    )
//            .setBackoffCriteria(
//                BackoffPolicy.EXPONENTIAL,
//                5,
//                TimeUnit.MINUTES
//            )
      .build()
    workManager.enqueueUniquePeriodicWork(
      UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request,
    )
  }

  private companion object {
    const val UNIQUE_WORK_NAME = "weather_sync"
  }
}
