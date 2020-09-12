package com.example.forecastify

import android.app.Application
import com.example.forecastify.data.WeatherAPI
import com.example.forecastify.data.db.WeatherDB
import com.example.forecastify.data.network.ConnectivityInterceptor
import com.example.forecastify.data.network.ConnectivityInterceptorImpl
import com.example.forecastify.data.network.WeatherNetworkDataSource
import com.example.forecastify.data.network.WeatherNetworkDataSourceImpl
import com.example.forecastify.data.repository.ForecastRepository
import com.example.forecastify.data.repository.ForecastRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.androidSupportModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class ForecastApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidSupportModule(this@ForecastApplication))

        bind() from singleton { WeatherDB(instance()) }
        bind() from singleton { instance<WeatherDB>().currentWeatherDAO() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherAPI(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance()) }
    }
}