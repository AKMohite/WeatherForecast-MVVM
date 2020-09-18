package com.example.forecastify.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecastify.data.WeatherAPI
import com.example.forecastify.data.network.response.CurrentWeatherResponse
import com.example.forecastify.data.network.response.FutureWeatherResponse
import com.example.forecastify.internal.NoInternetConnectivityException
import com.example.forecastify.internal.UnitType
import com.example.forecastify.internal.convertMetricToImperial
import com.google.gson.Gson
import kotlinx.coroutines.delay


const val FORECAST_DAYS_COUNT = 1 // todo change to request no of days
class WeatherNetworkDataSourceImpl(
    private val apiWeatherService: WeatherAPI
) : WeatherNetworkDataSource {

    private val _fetchedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()

    override val fetchedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _fetchedCurrentWeather

    private val _fetchedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val fetchedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _fetchedFutureWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val fetchCurrentWeather = apiWeatherService
                .getCurrentWeather(location)
                .await()
            fetchCurrentWeather.currentWeatherEntry.apply {
                temperatureF = temperatureC.convertMetricToImperial(UnitType.TEMPERATURE)
                windSpeedMps = windSpeedKmph.convertMetricToImperial(UnitType.WIND_SPEED)
                precipIn = precipMm.convertMetricToImperial(UnitType.PRECIPITATION)
                feelslikeF = feelslikeC.convertMetricToImperial(UnitType.TEMPERATURE)
                visibilityMi = visibilityKm.convertMetricToImperial(UnitType.VISIBILITY)
            }
            _fetchedCurrentWeather.postValue(fetchCurrentWeather)
        } catch (e: Exception){
            Log.e("NetworkSourceException", "Error: ${e.message}", e)
        }
    }

    override suspend fun fetchFutureWeather(location: String, languageCode: String) {
        try {
            /*val fetchedFutureWeather = apiWeatherService
                .getFutureWeather(location, FORECAST_DAYS_COUNT)
                .await()*/ // todo mocked response no access to api
/*------------------------API string response------------------------*/
            val mockedResponse = """{"forecast":{"2020-09-07":{"astro":{"moon_illumination":54,"moon_phase":"First Quarter","moonrise":"03:33 PM","moonset":"12:17 AM","sunrise":"06:28 AM","sunset":"07:19 PM"},"avgtemp":21.0,"date":"2020-09-07","date_epoch":1567814400,"hourly":[{"chanceoffog":0,"chanceoffrost":0,"chanceofhightemp":0,"chanceofovercast":90,"chanceofrain":0,"chanceofremdry":87,"chanceofsnow":0,"chanceofsunshine":15,"chanceofthunder":0,"chanceofwindy":0,"cloudcover":75,"dewpoint":12,"feelslike":18,"heatindex":18,"humidity":68,"precip":0.0,"pressure":1008,"temperature":18,"time":"0","uv_index":0,"visibility":10.0,"weather_code":122,"weather_descriptions":["Overcast"],"weather_icons":["http://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0004_black_low_cloud.png"],"wind_degree":15,"wind_dir":"NNE","wind_speed":28.0,"windchill":18,"windgust":35}],"maxtemp":25,"mintemp":17,"sunhour":10.3,"totalsnow":0,"uv_index":5},"2020-09-08":{"astro":{"moon_illumination":54,"moon_phase":"First Quarter","moonrise":"03:33 PM","moonset":"12:17 AM","sunrise":"06:28 AM","sunset":"07:19 PM"},"avgtemp":21.0,"date":"2020-09-08","date_epoch":1567814400,"hourly":[{"chanceoffog":0,"chanceoffrost":0,"chanceofhightemp":0,"chanceofovercast":90,"chanceofrain":0,"chanceofremdry":87,"chanceofsnow":0,"chanceofsunshine":15,"chanceofthunder":0,"chanceofwindy":0,"cloudcover":75,"dewpoint":12,"feelslike":18,"heatindex":18,"humidity":68,"precip":0.0,"pressure":1008,"temperature":18,"time":"0","uv_index":0,"visibility":10.0,"weather_code":122,"weather_descriptions":["Overcast"],"weather_icons":["http://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0004_black_low_cloud.png"],"wind_degree":15,"wind_dir":"NNE","wind_speed":28.0,"windchill":18,"windgust":35}],"maxtemp":25,"mintemp":17,"sunhour":10.3,"totalsnow":0,"uv_index":5},"2020-09-09":{"astro":{"moon_illumination":54,"moon_phase":"First Quarter","moonrise":"03:33 PM","moonset":"12:17 AM","sunrise":"06:28 AM","sunset":"07:19 PM"},"avgtemp":21.0,"date":"2020-09-09","date_epoch":1567814400,"hourly":[{"chanceoffog":0,"chanceoffrost":0,"chanceofhightemp":0,"chanceofovercast":90,"chanceofrain":0,"chanceofremdry":87,"chanceofsnow":0,"chanceofsunshine":15,"chanceofthunder":0,"chanceofwindy":0,"cloudcover":75,"dewpoint":12,"feelslike":18,"heatindex":18,"humidity":68,"precip":0.0,"pressure":1008,"temperature":18,"time":"0","uv_index":0,"visibility":10.0,"weather_code":122,"weather_descriptions":["Overcast"],"weather_icons":["http://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0004_black_low_cloud.png"],"wind_degree":15,"wind_dir":"NNE","wind_speed":28.0,"windchill":18,"windgust":35}],"maxtemp":25,"mintemp":17,"sunhour":10.3,"totalsnow":0,"uv_index":5},"2020-09-10":{"astro":{"moon_illumination":54,"moon_phase":"First Quarter","moonrise":"03:33 PM","moonset":"12:17 AM","sunrise":"06:28 AM","sunset":"07:19 PM"},"avgtemp":21.0,"date":"2020-09-10","date_epoch":1567814400,"hourly":[{"chanceoffog":0,"chanceoffrost":0,"chanceofhightemp":0,"chanceofovercast":90,"chanceofrain":0,"chanceofremdry":87,"chanceofsnow":0,"chanceofsunshine":15,"chanceofthunder":0,"chanceofwindy":0,"cloudcover":75,"dewpoint":12,"feelslike":18,"heatindex":18,"humidity":68,"precip":0.0,"pressure":1008,"temperature":18,"time":"0","uv_index":0,"visibility":10.0,"weather_code":122,"weather_descriptions":["Overcast"],"weather_icons":["http://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0004_black_low_cloud.png"],"wind_degree":15,"wind_dir":"NNE","wind_speed":28.0,"windchill":18,"windgust":35}],"maxtemp":25,"mintemp":17,"sunhour":10.3,"totalsnow":0,"uv_index":5},"2020-09-11":{"astro":{"moon_illumination":54,"moon_phase":"First Quarter","moonrise":"03:33 PM","moonset":"12:17 AM","sunrise":"06:28 AM","sunset":"07:19 PM"},"avgtemp":21.0,"date":"2020-09-11","date_epoch":1567814400,"hourly":[{"chanceoffog":0,"chanceoffrost":0,"chanceofhightemp":0,"chanceofovercast":90,"chanceofrain":0,"chanceofremdry":87,"chanceofsnow":0,"chanceofsunshine":15,"chanceofthunder":0,"chanceofwindy":0,"cloudcover":75,"dewpoint":12,"feelslike":18,"heatindex":18,"humidity":68,"precip":0.0,"pressure":1008,"temperature":18,"time":"0","uv_index":0,"visibility":10.0,"weather_code":122,"weather_descriptions":["Overcast"],"weather_icons":["http://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0004_black_low_cloud.png"],"wind_degree":15,"wind_dir":"NNE","wind_speed":28.0,"windchill":18,"windgust":35}],"maxtemp":25,"mintemp":17,"sunhour":10.3,"totalsnow":0,"uv_index":5},"2020-09-12":{"astro":{"moon_illumination":54,"moon_phase":"First Quarter","moonrise":"03:33 PM","moonset":"12:17 AM","sunrise":"06:28 AM","sunset":"07:19 PM"},"avgtemp":21.0,"date":"2020-09-12","date_epoch":1567814400,"hourly":[{"chanceoffog":0,"chanceoffrost":0,"chanceofhightemp":0,"chanceofovercast":90,"chanceofrain":0,"chanceofremdry":87,"chanceofsnow":0,"chanceofsunshine":15,"chanceofthunder":0,"chanceofwindy":0,"cloudcover":75,"dewpoint":12,"feelslike":18,"heatindex":18,"humidity":68,"precip":0.0,"pressure":1008,"temperature":18,"time":"0","uv_index":0,"visibility":10.0,"weather_code":122,"weather_descriptions":["Overcast"],"weather_icons":["http://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0004_black_low_cloud.png"],"wind_degree":15,"wind_dir":"NNE","wind_speed":28.0,"windchill":18,"windgust":35}],"maxtemp":25,"mintemp":17,"sunhour":10.3,"totalsnow":0,"uv_index":5}},"location":{"country":"United States of America","id":0,"lat":"40.714","localtime":"2020-09-07 11:38","localtime_epoch":1567856280,"lon":"-74.006","name":"New York","region":"New York","timezone_id":"America/New_York","utc_offset":"-4.0"},"request":{"language":"en","query":"New York, United States of America","type":"City","unit":"m"}}"""
            delay(3000)
            val gson = Gson()
            val fetchedFutureWeather = gson.fromJson(
                mockedResponse,
                FutureWeatherResponse::class.java
            )
/*------------------------API string response------------------------*/
            _fetchedFutureWeather.postValue(fetchedFutureWeather)
        }
        catch (e: NoInternetConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}