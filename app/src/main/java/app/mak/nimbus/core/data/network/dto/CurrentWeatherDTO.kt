package app.mak.nimbus.core.data.network.dto


import app.mak.nimbus.core.domain.model.CurrentWeather
import app.mak.nimbus.core.domain.model.WeatherForecast
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherDTO(
    @SerialName("base")
    val base: String? = null,
    @SerialName("clouds")
    val clouds: CloudDTO? = null,
    @SerialName("cod")
    val cod: Int? = null,
    @SerialName("coord")
    val coord: CoordDTO? = null,
    @SerialName("dt")
    val dt: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("main")
    val main: MainDTo? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("sys")
    val sys: SysDTO? = null,
    @SerialName("timezone")
    val timezone: Int? = null,
    @SerialName("visibility")
    val visibility: Int? = null,
    @SerialName("weather")
    val weather: List<WeatherDTO?>? = null,
    @SerialName("wind")
    val wind: WindDTO? = null
)

fun CurrentWeatherDTO.toWeatherForecast(): WeatherForecast {
    return WeatherForecast(
        cityId = id.toString(),
        current = CurrentWeather(
            tempC = main?.temp ?: 0.0,
            feelsLikeC = (main?.feelsLike ?: 0).toDouble(),
            conditionCode = weather?.firstOrNull()?.id ?: 0,
            conditionText = weather?.firstOrNull()?.description ?: "",
            icon = weather?.firstOrNull()?.icon ?: "",
            humidity = main?.humidity ?: 0,
            windSpeedKph = wind?.speed ?: 0.0,
            pressure = main?.pressure ?: 0,
            uvIndex = 0.0,
            isDay = true,
            lastUpdated = 0L
        )
    )
}