package app.mak.atmosense.core.sync

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dev.zacsweers.metro.Inject

@Inject
class MetroWorkerFactory(
  private val workerFactories: Map<String, @JvmSuppressWildcards (Context, WorkerParameters) -> ListenableWorker>
) : WorkerFactory() {
  override fun createWorker(
    appContext: Context,
    workerClassName: String,
    workerParameters: WorkerParameters
  ): ListenableWorker? {
    return workerFactories[workerClassName]?.invoke(appContext, workerParameters)
  }
}
