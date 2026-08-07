package app.mak.atmosense.core.network.di

import app.mak.atmosense.core.common.di.OWMApiKey
import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.AppException
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
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
          val clientException =
            exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
          val exceptionResponse = clientException.response
          if (exceptionResponse.status == HttpStatusCode.NotFound) {
              val exceptionResponseText = exceptionResponse.bodyAsText()
            throw AppException(
              AppError.Unknown(
                exceptionResponse.status.value,
                exceptionResponseText
              ), exception
            )
          }
          throw handleKtorExceptions(exception)
            ?: AppException(AppError.Unknown(exceptionResponse.status.value), cause = exception)
        }
      }
    }
  }

  private suspend fun handleKtorExceptions(exception: Throwable): Throwable? {
//        TODO check for ktor exceptions instead java
    return when (exception) {
      is ClientRequestException -> {
//        val exceptionResponse = exception.response
//        val error = getErrorDTO(exceptionResponse)
//        return PocketAPIException(
//          code = exceptionResponse.status.value,
//          errorMsg = error?.message ?: ExceptionType.UNKNOWN.message,
//          throwable = exception
//        )
        return AppException(
          error = AppError.Unknown(exception.response.status.value),
          cause = exception
        )
      }
//            is java.net.SocketTimeoutException -> RequestTimeoutException(throwable = exception)
//            is java.io.IOException -> NoNetworkException()
      else -> null
    }
  }
}
