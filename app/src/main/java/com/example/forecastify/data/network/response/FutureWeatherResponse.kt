package com.example.forecastify.data.network.response


import com.example.forecastify.data.db.entity.WeatherLocationEntry
import com.google.gson.annotations.SerializedName


/*{"request":{"type":"City","query":"New York, United States of America","language":"en","unit":"m"},"location":{"name":"New York","country":"United States of America","region":"New York","lat":"40.714","lon":"-74.006","timezone_id":"America/New_York","localtime":"2019-09-07 11:38","localtime_epoch":1567856280,"utc_offset":"-4.0"},"current":{"observation_time":"03:38 PM","temperature":18,"weather_code":113,"weather_icons":["https://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0001_sunny.png"],"weather_descriptions":["Sunny"],"wind_speed":0,"wind_degree":345,"wind_dir":"NNW","pressure":1011,"precip":0,"humidity":58,"cloudcover":0,"feelslike":18,"uv_index":5,"visibility":16},"forecast":{"2019-09-07":{"date":"2019-09-07","date_epoch":1567814400,"astro":{"sunrise":"06:28 AM","sunset":"07:19 PM","moonrise":"03:33 PM","moonset":"12:17 AM","moon_phase":"First Quarter","moon_illumination":54},"mintemp":17,"maxtemp":25,"avgtemp":21,"totalsnow":0,"sunhour":10.3,"uv_index":5,"hourly":[{"time":"0","temperature":18,"wind_speed":28,"wind_degree":15,"wind_dir":"NNE","weather_code":122,"weather_icons":["https://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0004_black_low_cloud.png"],"weather_descriptions":["Overcast"],"precip":0,"humidity":68,"visibility":10,"pressure":1008,"cloudcover":75,"heatindex":18,"dewpoint":12,"windchill":18,"windgust":35,"feelslike":18,"chanceofrain":0,"chanceofremdry":87,"chanceofwindy":0,"chanceofovercast":90,"chanceofsunshine":15,"chanceoffrost":0,"chanceofhightemp":0,"chanceoffog":0,"chanceofsnow":0,"chanceofthunder":0,"uv_index":0}]}}}*/

data class FutureWeatherResponse(
    @SerializedName("forecast")
    val forecastContainer: Map<String, FutureWeatherInfo>,
    val location: WeatherLocationEntry,
    val request: Request
)

data class FutureWeatherInfo(
    val id: Int? = null,
    val astro: Astro,
    val avgtemp: Double,
    val date: String,
    @SerializedName("date_epoch")
    val dateEpoch: Int,
    val hourly: List<Hourly>,
    val maxtemp: Int,
    val mintemp: Int,
    val sunhour: Double,
    val totalsnow: Int,
    @SerializedName("uv_index")
    val uvIndex: Int
)

data class Astro(
    @SerializedName("moon_illumination")
    val moonIllumination: Int,
    @SerializedName("moon_phase")
    val moonPhase: String,
    val moonrise: String,
    val moonset: String,
    val sunrise: String,
    val sunset: String
)

data class Hourly(
    val chanceoffog: Int,
    val chanceoffrost: Int,
    val chanceofhightemp: Int,
    val chanceofovercast: Int,
    val chanceofrain: Int,
    val chanceofremdry: Int,
    val chanceofsnow: Int,
    val chanceofsunshine: Int,
    val chanceofthunder: Int,
    val chanceofwindy: Int,
    val cloudcover: Int,
    val dewpoint: Int,
    val feelslike: Int,
    val heatindex: Int,
    val humidity: Int,
    val precip: Double,
    val pressure: Int,
    val temperature: Int,
    val time: String,
    @SerializedName("uv_index")
    val uvIndex: Int,
    val visibility: Double,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("weather_descriptions")
    val weatherDescriptions: List<String>,
    @SerializedName("weather_icons")
    val weatherIcons: List<String>,
    @SerializedName("wind_degree")
    val windDegree: Int,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    val windchill: Int,
    val windgust: Int
)

data class Location(
    val country: String,
    val lat: String,
    val localtime: String,
    @SerializedName("localtime_epoch")
    val localtimeEpoch: Int,
    val lon: String,
    val name: String,
    val region: String,
    @SerializedName("timezone_id")
    val timezoneId: String,
    @SerializedName("utc_offset")
    val utcOffset: String
)