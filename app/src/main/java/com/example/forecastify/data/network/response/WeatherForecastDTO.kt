package com.example.forecastify.data.network.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherForecastDTO(
    @Json(name = "city")
    val city: CityDTO,
    @Json(name = "cnt")
    val cnt: Int,
    @Json(name = "cod")
    val cod: String,
    @Json(name = "list")
    val list: List<FutureDTO>,
    @Json(name = "message")
    val message: Double
)

@JsonClass(generateAdapter = true)
data class CityDTO(
    @Json(name = "coord")
    val coord: CoordinateDTO,
    @Json(name = "country")
    val country: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "population")
    val population: Int,
    @Json(name = "sunrise")
    val sunrise: Int?,
    @Json(name = "sunset")
    val sunset: Int?,
    @Json(name = "timezone")
    val timezone: Int
)


@JsonClass(generateAdapter = true)
data class FutureDTO(
    @Json(name = "clouds")
    val clouds: Int,
    @Json(name = "deg")
    val deg: Int,
    @Json(name = "dt")
    val dt: Int,
    @Json(name = "feels_like")
    val feelsLike: FeelsLikeDTO,
    @Json(name = "gust")
    val gust: Double,
    @Json(name = "humidity")
    val humidity: Int,
    @Json(name = "pop")
    val pop: Int,
    @Json(name = "pressure")
    val pressure: Int,
    @Json(name = "speed")
    val speed: Double,
    @Json(name = "sunrise")
    val sunrise: Int,
    @Json(name = "sunset")
    val sunset: Int,
    @Json(name = "temp")
    val temp: TempDTO,
    @Json(name = "weather")
    val weather: List<WeatherDTO>
)

@JsonClass(generateAdapter = true)
data class FeelsLikeDTO(
    @Json(name = "day")
    val day: Double,
    @Json(name = "eve")
    val eve: Double,
    @Json(name = "morn")
    val morn: Double,
    @Json(name = "night")
    val night: Double
)

@JsonClass(generateAdapter = true)
data class TempDTO(
    @Json(name = "day")
    val day: Double,
    @Json(name = "eve")
    val eve: Double,
    @Json(name = "max")
    val max: Double,
    @Json(name = "min")
    val min: Double,
    @Json(name = "morn")
    val morn: Double,
    @Json(name = "night")
    val night: Double
)
