package com.example.forecastify.data.network.response


import com.example.forecastify.data.db.entity.CurrentWeatherEntry
import com.example.forecastify.data.db.entity.WeatherLocationEntry
import com.google.gson.annotations.SerializedName
/*{"request":{"type":"City","query":"New York, United States of America","language":"en","unit":"m"},"location":{"name":"New York","country":"United States of America","region":"New York","lat":"40.714","lon":"-74.006","timezone_id":"America/New_York","localtime":"2020-09-12 01:49","localtime_epoch":1599875340,"utc_offset":"-4.0"},"current":{"observation_time":"05:49 AM","temperature":19,"weather_code":122,"weather_icons":["https://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0004_black_low_cloud.png"],"weather_descriptions":["Overcast"],"wind_speed":11,"wind_degree":60,"wind_dir":"ENE","pressure":1026,"precip":0.1,"humidity":70,"cloudcover":100,"feelslike":19,"uv_index":1,"visibility":16,"is_day":"no"}}*/
data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocationEntry,
    val request: Request
)

data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)