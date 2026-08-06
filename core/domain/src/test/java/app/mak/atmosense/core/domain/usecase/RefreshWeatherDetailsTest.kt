package app.mak.atmosense.core.domain.usecase

import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.AppException
import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.domain.testing.FakeWeatherRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RefreshWeatherDetailsTest {

  private val fakeWeatherRepository = FakeWeatherRepository()
  private val useCase = RefreshWeatherDetails(fakeWeatherRepository)

  @Test
  fun `when both fetches succeed, returns success`() = runTest {
    // given
    fakeWeatherRepository.fetchResult = AppResult.Success(Unit)
    fakeWeatherRepository.fetchForecastResult = AppResult.Success(Unit)

    // when
    val result = useCase(cityId = 1L)

    // then
    assertEquals(AppResult.Success(Unit), result)
  }

  @Test
  fun `when current weather fetch fails, returns failure`() = runTest {
    // given
    val error = AppError.NoInternet
    fakeWeatherRepository.fetchResult = AppResult.Failure(error)
    fakeWeatherRepository.fetchForecastResult = AppResult.Success(Unit)

    // when
    val result = useCase(cityId = 1L)

    // then
    assertTrue(result is AppResult.Failure)
    assertEquals(error, (result as AppResult.Failure).error)
  }

  @Test
  fun `when forecast fetch fails, returns failure`() = runTest {
    // given
    val error = AppError.Timeout
    fakeWeatherRepository.fetchResult = AppResult.Success(Unit)
    fakeWeatherRepository.fetchForecastResult = AppResult.Failure(error)

    // when
    val result = useCase(cityId = 1L)

    // then
    assertTrue(result is AppResult.Failure)
    assertEquals(error, (result as AppResult.Failure).error)
  }

  @Test
  fun `when repository throws AppException, returns failure with error`() = runTest {
    // given
    val error = AppError.EntityNotFound
    val repo = FakeWeatherRepository()
    repo.error = AppException(error)
    val useCase = RefreshWeatherDetails(repo)

    // when
    val result = useCase(cityId = 1L)

    // then
    assertTrue(result is AppResult.Failure)
    assertEquals(error, (result as AppResult.Failure).error)
  }

  @Test
  fun `when repository throws generic exception, returns unknown failure`() = runTest {
    // given
    val repo = FakeWeatherRepository()
    repo.error = RuntimeException("Boom")
    val useCase = RefreshWeatherDetails(repo)

    // when
    val result = useCase(cityId = 1L)

    // then
    assertTrue(result is AppResult.Failure)
    assertTrue((result as AppResult.Failure).error is AppError.Unknown)
    assertEquals("Boom", (result.error as AppError.Unknown).message)
  }
}
