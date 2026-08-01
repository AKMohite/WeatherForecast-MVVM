package app.mak.atmosense.core.network

import app.mak.atmosense.core.network.dto.CurrentWeatherDTO
import app.mak.atmosense.core.network.utils.safeApiCall
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface WeatherAPI {
  suspend fun getCurrentWeather(queries: Map<String, String>): CurrentWeatherDTO
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

  private fun getAllQueries(queries: Map<String, String>) = queries.map {
    "${it.key}=${it.value}"
  }.joinToString("&")
}
