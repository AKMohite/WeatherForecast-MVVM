package app.mak.atmosense.core.domain.usecase

import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.domain.repository.WeatherRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

@Inject
class ObserveCitiesWeather(
  private val weatherRepository: WeatherRepository
) {
  operator fun invoke(): Flow<List<CityWeather>> {
    return weatherRepository.observeCitiesWeather()
  }
}
