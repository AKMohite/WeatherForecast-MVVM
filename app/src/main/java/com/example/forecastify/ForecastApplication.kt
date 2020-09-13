package com.example.forecastify

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.example.forecastify.data.WeatherAPI
import com.example.forecastify.data.db.WeatherDB
import com.example.forecastify.data.network.ConnectivityInterceptor
import com.example.forecastify.data.network.ConnectivityInterceptorImpl
import com.example.forecastify.data.network.WeatherNetworkDataSource
import com.example.forecastify.data.network.WeatherNetworkDataSourceImpl
import com.example.forecastify.data.provider.LocationProvider
import com.example.forecastify.data.provider.LocationProviderImpl
import com.example.forecastify.data.provider.UnitProvider
import com.example.forecastify.data.provider.UnitProviderImpl
import com.example.forecastify.data.repository.ForecastRepository
import com.example.forecastify.data.repository.ForecastRepositoryImpl
import com.example.forecastify.ui.weather.current.CurrentWeatherViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.androidSupportModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidSupportModule(this@ForecastApplication))

        bind() from singleton { WeatherDB(instance()) }
        bind() from singleton { instance<WeatherDB>().currentWeatherDAO() }
        bind() from singleton { instance<WeatherDB>().weatherLocationDAO() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherAPI(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance(), instance(), instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}