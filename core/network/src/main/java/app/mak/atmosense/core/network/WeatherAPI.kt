package app.mak.atmosense.core.network

import app.mak.atmosense.core.network.dto.CurrentWeatherDTO
import app.mak.atmosense.core.network.dto.ForecastWeatherDTO
import app.mak.atmosense.core.network.dto.LocationDTO
import app.mak.atmosense.core.network.utils.safeApiCall
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface WeatherAPI {
  suspend fun getCurrentWeather(queries: Map<String, String>): CurrentWeatherDTO
  suspend fun searchLocations(queries: Map<String, String>): List<LocationDTO>
  suspend fun getForecastWeather(queries: Map<String, String>): ForecastWeatherDTO
}

@ContributesBinding(scope = AppScope::class)
@Inject
class OpenWeatherMapAPI(
  private val httpClient: HttpClient
) : WeatherAPI {
  override suspend fun getCurrentWeather(queries: Map<String, String>): CurrentWeatherDTO {
    return safeApiCall {
      val queries = getAllQueries(queries)
      httpClient.get("/data/2.5/weather?$queries").body<CurrentWeatherDTO>()
    }
  }

  override suspend fun searchLocations(queries: Map<String, String>): List<LocationDTO> {
    return safeApiCall {
      val queries = getAllQueries(queries)
      httpClient.get("/geo/1.0/direct?$queries").body<List<LocationDTO>>()
    }
  }

  override suspend fun getForecastWeather(queries: Map<String, String>): ForecastWeatherDTO {
    return safeApiCall {
      val queries = getAllQueries(queries)
      httpClient.get("/data/2.5/forecast?$queries").body<ForecastWeatherDTO>()
    }
  }

  private fun getAllQueries(queries: Map<String, String>) = queries.map {
    "${it.key}=${it.value}"
  }.joinToString("&")
}
