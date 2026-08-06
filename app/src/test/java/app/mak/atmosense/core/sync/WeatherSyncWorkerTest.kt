package app.mak.atmosense.core.sync

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.coroutines.cancellation.CancellationException

@RunWith(RobolectricTestRunner::class)
class WeatherSyncWorkerTest {

  private lateinit var context: Context
  private lateinit var fakeSyncRepository: FakeSyncRepository

  @Before
  fun setUp() {
    context = ApplicationProvider.getApplicationContext()
    fakeSyncRepository = FakeSyncRepository()
  }

  @Test
  fun `doWork returns success when sync is successful`() = runTest {
    // Given
    val worker = TestListenableWorkerBuilder<WeatherSyncWorker>(context)
      .setWorkerFactory(TestWorkerFactory(fakeSyncRepository))
      .build()

    // When
    val result = worker.doWork()

    // Then
    assertEquals(ListenableWorker.Result.success(), result)
    assertEquals(1, fakeSyncRepository.syncCallCount)
  }

  @Test
  fun `doWork returns failure when sync throws exception`() = runTest {
    // Given
    fakeSyncRepository.shouldThrow = true
    val worker = TestListenableWorkerBuilder<WeatherSyncWorker>(context)
      .setWorkerFactory(TestWorkerFactory(fakeSyncRepository))
      .build()

    // When
    val result = worker.doWork()

    // Then
    assertEquals(ListenableWorker.Result.failure(), result)
    assertEquals(1, fakeSyncRepository.syncCallCount)
  }

  @Test
  fun `doWork rethrows CancellationException`() = runTest {
    // Given
    fakeSyncRepository.shouldThrowCancellation = true
    val worker = TestListenableWorkerBuilder<WeatherSyncWorker>(context)
      .setWorkerFactory(TestWorkerFactory(fakeSyncRepository))
      .build()

    // When & Then
    try {
      worker.doWork()
      fail("Should have thrown CancellationException")
    } catch (e: CancellationException) {
      // Expected
    }
  }

  private class FakeSyncRepository : SyncRepository {
    var syncCallCount = 0
    var shouldThrow = false
    var shouldThrowCancellation = false

    override suspend fun sync() {
      syncCallCount++
      if (shouldThrowCancellation) throw CancellationException("Cancelled")
      if (shouldThrow) throw Exception("Sync failed")
    }
  }

  private class TestWorkerFactory(
    private val syncRepository: SyncRepository
  ) : androidx.work.WorkerFactory() {
    override fun createWorker(
      appContext: Context,
      workerClassName: String,
      workerParameters: androidx.work.WorkerParameters
    ): ListenableWorker? {
      return if (workerClassName == WeatherSyncWorker::class.java.name) {
        WeatherSyncWorker(appContext, workerParameters, syncRepository)
      } else {
        null
      }
    }
  }
}
