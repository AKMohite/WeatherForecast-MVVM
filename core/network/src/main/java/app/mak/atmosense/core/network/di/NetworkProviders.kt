package app.mak.atmosense.core.network.di

import app.mak.atmosense.core.common.di.OWMApiKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val API_HOST = "api.openweathermap.org"

@ContributesTo(AppScope::class)
interface NetworkProviders {

  @SingleIn(AppScope::class)
  @Provides
  fun provideJson(): Json {
    return Json {
      isLenient = true
      ignoreUnknownKeys = true
      useAlternativeNames = false
    }
  }

  @SingleIn(AppScope::class)
  @Provides
  fun provideHttpClient(
    json: Json,
    @OWMApiKey owmApiKey: String,
  ): HttpClient {
    return HttpClient {
      install(ContentNegotiation) {
        json(json)
      }
      defaultRequest {
        url {
          protocol = URLProtocol.HTTPS
          host = API_HOST
          parameters.append("appId", owmApiKey)
        }
      }
      HttpResponseValidator {
        handleResponseExceptionWithRequest { exception, request ->
          println(exception)
          println(request)
          /*val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
          val exceptionResponse = clientException.response
          if (exceptionResponse.status == HttpStatusCode.NotFound) {
              val exceptionResponseText = exceptionResponse.bodyAsText()
              throw MissingPageException(exceptionResponse, exceptionResponseText)
          }*/
//                throw handleKtorExceptions(exception) ?: UnknownAPIException(throwable = exception)
        }
      }
    }
  }
}
