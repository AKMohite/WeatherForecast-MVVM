package app.mak.atmosense.core.network

import app.mak.atmosense.core.network.dto.CurrentWeatherDTO
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface WeatherAPI {
  suspend fun getCurrentWeather(): String
}

@ContributesBinding(scope = AppScope::class)
@Inject
class OpenWeatherMapAPI(
  private val httpClient: HttpClient
) : WeatherAPI {
  override suspend fun getCurrentWeather(): String {
    val queryMap = mapOf(
      "lat" to "72.19",
      "lon" to "17.84",
//      "appid" to "asdasd"
    )
    val queries = getAllQueries(queryMap)
    return httpClient.get("/data/2.5/weather?$queries").body<CurrentWeatherDTO>().toString()
  }

  private fun getAllQueries(queries: Map<String, String>) = queries.map {
    "${it.key}=${it.value}"
  }.joinToString("&")
}
