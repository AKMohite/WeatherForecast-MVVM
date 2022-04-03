package com.example.forecastify.data.network.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentWeatherDTO(
    @Json(name = "base")
    val base: String,
    @Json(name = "clouds")
    val clouds: CloudsDTO,
    @Json(name = "cod")
    val cod: Int,
    @Json(name = "coord")
    val coord: CoordinateDTO,
    @Json(name = "dt")
    val dt: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "main")
    val main: MainDTO,
    @Json(name = "name")
    val name: String,
    @Json(name = "sys")
    val sys: SysDTO,
    @Json(name = "timezone")
    val timezone: Int,
    @Json(name = "visibility")
    val visibility: Int,
    @Json(name = "weather")
    val weather: List<WeatherDTO>,
    @Json(name = "wind")
    val wind: WindDTO
)

@JsonClass(generateAdapter = true)
data class CloudsDTO(
    @Json(name = "all")
    val all: Int
)

@JsonClass(generateAdapter = true)
data class CoordinateDTO(
    @Json(name = "lat")
    val lat: Double,
    @Json(name = "lon")
    val lon: Double
)

@JsonClass(generateAdapter = true)
data class MainDTO(
    @Json(name = "feels_like")
    val feelsLike: Double,
    @Json(name = "grnd_level")
    val grndLevel: Int,
    @Json(name = "humidity")
    val humidity: Int,
    @Json(name = "pressure")
    val pressure: Int,
    @Json(name = "sea_level")
    val seaLevel: Int,
    @Json(name = "temp")
    val temp: Double,
    @Json(name = "temp_max")
    val tempMax: Double,
    @Json(name = "temp_min")
    val tempMin: Double,
    @Json(name = "temp_kf")
    val tempKf: Double
)

@JsonClass(generateAdapter = true)
data class SysDTO(
    @Json(name = "country")
    val country: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "sunrise")
    val sunrise: Int,
    @Json(name = "sunset")
    val sunset: Int,
    @Json(name = "type")
    val type: Int,
    @Json(name = "pod")
    val pod: String?
)

@JsonClass(generateAdapter = true)
data class WeatherDTO(
    @Json(name = "description")
    val description: String,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "main")
    val main: String
)

@JsonClass(generateAdapter = true)
data class WindDTO(
    @Json(name = "deg")
    val deg: Int,
    @Json(name = "gust")
    val gust: Double,
    @Json(name = "speed")
    val speed: Double
)