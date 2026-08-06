package app.mak.atmosense.core.location

import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.LocationCoordinate
import app.mak.atmosense.core.location.testing.FakeLocationService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test


class LocationAccessCoordinatorTest {

  private val fakeLocationService = FakeLocationService()
  private val coordinator = LocationAccessCoordinator(fakeLocationService)

  @Test
  fun `permission granted and a successful fetch returns Available with the coordinates`() =
    runTest {
      // given
      val coordinate = LocationCoordinate(19.0760, 72.8777)
      fakeLocationService.result = AppResult.Success(coordinate)
      // when
      val result = coordinator.resolveCurrentLocation()
      // then
      val expected = LocationAccessResult.Available(coordinate)
      assertEquals(expected, result)
    }

  @Test
  fun `permission granted but the fetch fails maps AppError through to Unavailable`() = runTest {
    fakeLocationService.result = AppResult.Failure(AppError.Timeout)

    val result = coordinator.resolveCurrentLocation()

    assertEquals(LocationAccessResult.Unavailable(AppError.Timeout), result)
  }

}
