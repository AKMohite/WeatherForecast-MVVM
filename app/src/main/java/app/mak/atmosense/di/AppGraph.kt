package app.mak.atmosense.di

import android.content.Context
import app.mak.atmosense.core.sync.MetroWorkerFactory
import app.mak.atmosense.core.sync.SyncScheduler
import com.slack.circuit.foundation.Circuit
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Provides

@DependencyGraph(AppScope::class)
interface AppGraph {
  val repository: DummyRepository
  val circuit: Circuit
  val syncScheduler: SyncScheduler
  val workerFactory: MetroWorkerFactory

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(@Provides context: Context): AppGraph
  }
}


interface DummyRepository {
  fun getData(): String
}

// This class is automatically bound as Repository in any graph with AppScope
@ContributesBinding(AppScope::class)
@Inject
class DummyRepositoryImpl() : DummyRepository {
  override fun getData(): String = "apiClient.fetch()"
}
