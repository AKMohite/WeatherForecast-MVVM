package app.mak.atmosense.feature.cities

import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.LocationCoordinate
import app.mak.atmosense.core.domain.testing.FakeWeatherRepository
import app.mak.atmosense.core.location.LocationAccessCoordinator
import app.mak.atmosense.core.location.testing.FakeLocationService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FetchCurrentLocationWeatherTest {

  private val fakeLocationService = FakeLocationService()
  private val locationCoordinator = LocationAccessCoordinator(fakeLocationService)
  private val fakeWeatherRepository = FakeWeatherRepository()
  private val useCase = FetchCurrentLocationWeather(locationCoordinator, fakeWeatherRepository)

  @Test
  fun `when location is available and weather fetch succeeds, returns success`() = runTest {
    // given
    val coordinate = LocationCoordinate(1.0, 2.0)
    fakeLocationService.result = AppResult.Success(coordinate)
    fakeWeatherRepository.fetchResult = AppResult.Success(Unit)

    // when
    val result = useCase()

    // then
    assertEquals(AppResult.Success(Unit), result)
    assertEquals(coordinate, fakeWeatherRepository.passedCoordinates)
  }

  @Test
  fun `when location is unavailable, returns failure with location error`() = runTest {
    // given
    val error = AppError.Timeout
    fakeLocationService.result = AppResult.Failure(error)

    // when
    val result = useCase()

    // then
    assertTrue(result is AppResult.Failure)
    assertEquals(error, (result as AppResult.Failure).error)
  }

  @Test
  fun `when location is available but weather fetch fails, returns failure with repository error`() =
    runTest {
      // given
      val coordinate = LocationCoordinate(1.0, 2.0)
      val error = AppResult.Failure(AppError.NoInternet)
      fakeLocationService.result = AppResult.Success(coordinate)
      fakeWeatherRepository.fetchResult = error

      // when
      val result = useCase()

      // then
      assertEquals(error, result)
    }

}
