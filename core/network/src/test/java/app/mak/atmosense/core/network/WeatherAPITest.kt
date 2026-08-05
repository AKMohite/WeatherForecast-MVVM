package app.mak.atmosense.core.network

import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.AppException
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class WeatherAPITest {

  private fun createMockHttpClient(handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData): HttpClient {
    return HttpClient(MockEngine(handler)) {
      install(ContentNegotiation) {
        json(Json {
          ignoreUnknownKeys = true
          isLenient = true
        })
      }
    }
  }

  @Test
  fun `getCurrentWeather calls correct URL and parses response`() = runTest {
    val mockClient = createMockHttpClient { request ->
      if (request.url.encodedPath == "/data/2.5/weather") {
        assertEquals("q=London&appid=test", request.url.encodedQuery)
        respond(
          content = """{"id": 2643743, "name": "London"}""",
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        )
      } else {
        respond("", status = HttpStatusCode.NotFound)
      }
    }

    val api = OpenWeatherMapAPI(mockClient)
    val result = api.getCurrentWeather(mapOf("q" to "London", "appid" to "test"))

    assertEquals(2643743L, result.cityId)
    assertEquals("London", result.name)
  }

  @Test
  fun `searchLocations calls correct URL and parses response`() = runTest {
    val mockClient = createMockHttpClient { request ->
      if (request.url.encodedPath == "/geo/1.0/direct") {
        assertEquals("q=Mumbai&limit=5", request.url.encodedQuery)
        respond(
          content = """[{"name": "Mumbai", "lat": 19.076, "lon": 72.8777}]""",
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        )
      } else {
        respond("", status = HttpStatusCode.NotFound)
      }
    }

    val api = OpenWeatherMapAPI(mockClient)
    val result = api.searchLocations(mapOf("q" to "Mumbai", "limit" to "5"))

    assertEquals(1, result.size)
    assertEquals("Mumbai", result[0].name)
    assertEquals(19.076, result[0].lat!!, 0.001)
  }

  @Test
  fun `getForecastWeather calls correct URL and parses response`() = runTest {
    val mockClient = createMockHttpClient { request ->
      if (request.url.encodedPath == "/data/2.5/forecast") {
        respond(
          content = """{"cnt": 40, "message": 0}""",
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        )
      } else {
        respond("", status = HttpStatusCode.NotFound)
      }
    }

    val api = OpenWeatherMapAPI(mockClient)
    val result = api.getForecastWeather(mapOf("q" to "London"))

    assertEquals(40, result.cnt)
    assertEquals(0, result.message)
  }

  @Test
  fun `IOException in httpClient is mapped to AppError NoInternet`() = runTest {
    val mockClient = HttpClient(MockEngine {
      throw IOException("No internet")
    })

    val api = OpenWeatherMapAPI(mockClient)

    try {
      api.getCurrentWeather(emptyMap())
      org.junit.Assert.fail("Expected AppException")
    } catch (e: AppException) {
      assertEquals(AppError.NoInternet, e.error)
    }
  }
}
