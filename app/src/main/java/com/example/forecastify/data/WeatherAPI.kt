package com.example.forecastify.data

import com.example.forecastify.BuildConfig
import com.example.forecastify.data.network.ConnectivityInterceptor
import com.example.forecastify.data.network.ConnectivityInterceptorImpl
import com.example.forecastify.data.network.response.CurrentWeatherResponse
import com.example.forecastify.data.network.response.FutureWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = BuildConfig.weather_api_key
const val API_BASE_URL = "http://api.weatherstack.com/"
//api.weatherstack.com/current?access_key=API_KEY&query=&language
interface WeatherAPI {

//    todo change default language we have to check

    @GET("current")
    fun getCurrentWeather(
        @Query("query") location: String
    ): Deferred<CurrentWeatherResponse>

    @GET("forecast")
    fun getFutureWeather(
        @Query("query") location: String,
        @Query("forecast_days") days: Int
    ): Deferred<FutureWeatherResponse>

    companion object{
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): WeatherAPI{
            val requestInterceptor = Interceptor{ chain->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key", API_KEY)
                    .build()

                val request = chain.request()
                                .newBuilder()
                                .url(url)
                                .build()

                return@Interceptor chain.proceed(request)

            }

            val okHttpClient = OkHttpClient.Builder()
                                .addInterceptor(requestInterceptor)
                                .addInterceptor(connectivityInterceptor)
                                .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(API_BASE_URL)
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(WeatherAPI::class.java)
        }
    }
}