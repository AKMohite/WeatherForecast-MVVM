package com.example.forecastify

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.forecastify.data.provider.*
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ForecastApplication: Application() {
//    todo remove below comment
    /*override val kodein = Kodein.lazy {
        import(androidSupportModule(this@ForecastApplication))

        bind() from singleton { WeatherDB(instance()) }
        bind() from singleton { instance<WeatherDB>().currentWeatherDAO() }
        bind() from singleton { instance<WeatherDB>().weatherLocationDAO() }
        bind() from singleton { instance<WeatherDB>().futureWeatherDAO() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherAPI(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<ThemeProvider>() with singleton { ThemeProviderImpl(instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance(), instance(), instance(), instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
        bind() from provider { FutureListWeatherViewModelFactory(instance(), instance()) }
        bind() from factory { detailDate: LocalDate -> FutureDetailViewModelFactory(detailDate, instance(), instance()) }
    }*/

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        val themeProvider :ThemeProvider = ThemeProviderImpl(this) // todo change to DI
        val theme = themeProvider.getThemeFromPreferences()
        AppCompatDelegate.setDefaultNightMode(theme)
    }
}