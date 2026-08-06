package app.mak.atmosense.core.sync.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import app.mak.atmosense.core.sync.WeatherSyncWorker
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.StringKey

@ContributesTo(AppScope::class)
interface WorkManagerProviders {

  @SingleIn(AppScope::class)
  @Provides
  fun provideWorkManager(context: Context): WorkManager = WorkManager.getInstance(context)

  @Multibinds
  fun workerFactories(): Map<String, @JvmSuppressWildcards (Context, WorkerParameters) -> ListenableWorker>

  @Provides
  @IntoMap
  @StringKey("app.mak.atmosense.core.sync.WeatherSyncWorker")
  fun provideWeatherSyncWorkerFactory(
    factory: WeatherSyncWorker.Factory
  ): (Context, WorkerParameters) -> ListenableWorker {
    return factory::create
  }
}
