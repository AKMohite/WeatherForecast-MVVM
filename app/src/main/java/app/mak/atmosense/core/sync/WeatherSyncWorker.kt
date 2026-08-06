package app.mak.atmosense.core.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlin.coroutines.cancellation.CancellationException

class WeatherSyncWorker @AssistedInject constructor(
  @Assisted context: Context,
  @Assisted params: WorkerParameters,
  private val syncRepository: SyncRepository
) : CoroutineWorker(context, params) {

  override suspend fun doWork(): Result {
    return try {
      syncRepository.sync()
      Result.success()
    } catch (e: Exception) {
      if (e is CancellationException) throw e
      Result.failure()
    }
  }

  @AssistedFactory
  interface Factory {
    fun create(context: Context, params: WorkerParameters): WeatherSyncWorker
  }
}

