package com.example.forecastify.data.network.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourlyWeatherForecastDTO(
    @Json(name = "city")
    val city: CityDTO,
    @Json(name = "cnt")
    val cnt: Int,
    @Json(name = "cod")
    val cod: String,
    @Json(name = "list")
    val list: List<HourlyFutureDTO>,
    @Json(name = "message")
    val message: Int
)

@JsonClass(generateAdapter = true)
data class HourlyFutureDTO(
    @Json(name = "clouds")
    val clouds: CloudsDTO,
    @Json(name = "dt")
    val dt: Int,
    @Json(name = "dt_txt")
    val dtTxt: String,
    @Json(name = "main")
    val main: MainDTO,
    @Json(name = "pop")
    val pop: Int,
    @Json(name = "sys")
    val sys: SysDTO,
    @Json(name = "visibility")
    val visibility: Int,
    @Json(name = "weather")
    val weather: List<WeatherDTO>,
    @Json(name = "wind")
    val wind: WindDTO
)
