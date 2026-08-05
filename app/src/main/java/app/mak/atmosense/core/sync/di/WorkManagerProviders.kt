package app.mak.atmosense.core.sync.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import app.mak.atmosense.core.sync.WeatherSyncWorker
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface WorkManagerProviders {

  @SingleIn(AppScope::class)
  @Provides
  fun provideWorkManager(context: Context): WorkManager = WorkManager.getInstance(context)

  @Multibinds
  fun workerFactories(): Map<String, @JvmSuppressWildcards (Context, WorkerParameters) -> ListenableWorker>

  @Provides
  fun provideWeatherSyncWorkerFactory(
    factory: WeatherSyncWorker.Factory
  ): Map<String, @JvmSuppressWildcards (Context, WorkerParameters) -> ListenableWorker> {
    return mapOf(WeatherSyncWorker::class.java.name to factory::create)
  }
}
