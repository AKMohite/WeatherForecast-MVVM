package app.mak.nimbus.core.data.network

import app.mak.nimbus.core.data.network.dto.CurrentWeatherDTO
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherApi {

    @GET("weather")
    suspend fun getCurrentWeather(
        @QueryMap queries: Map<String, String>
    ): CurrentWeatherDTO

}
