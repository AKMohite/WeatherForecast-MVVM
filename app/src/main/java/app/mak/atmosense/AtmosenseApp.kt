package app.mak.atmosense

import android.app.Application
import androidx.work.Configuration
import app.mak.atmosense.di.AppGraph
import dev.zacsweers.metro.createGraphFactory

class AtmosenseApp : Application(), Configuration.Provider {

  lateinit var appGraph: AppGraph
    private set

  override fun onCreate() {
    super.onCreate()
    appGraph = createGraphFactory<AppGraph.Factory>().create(this)

    appGraph.syncScheduler.schedulePeriodicSync()
  }

  override val workManagerConfiguration: Configuration
    get() = Configuration.Builder()
      .setWorkerFactory(appGraph.workerFactory)
      .build()
}
