package app.mak.nimbus.core.data.repository

import app.mak.nimbus.core.data.network.WeatherApi
import app.mak.nimbus.core.data.network.dto.CurrentWeatherDTO
import app.mak.nimbus.core.data.network.dto.toWeatherForecast
import app.mak.nimbus.core.data.networkBoundResource
import app.mak.nimbus.core.domain.model.WeatherForecast
import app.mak.nimbus.core.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
): WeatherRepository {
    override suspend fun getCurrentWeather(
        city: String,
        lat: Double,
        lon: Double
    ): WeatherForecast = withContext(Dispatchers.IO) {
        val queries = mapOf(
            "lat" to lat.toString(),
            "lon" to lon.toString()
        )
        val response = weatherApi.getCurrentWeather(queries)
        response.toWeatherForecast()
    }

}
