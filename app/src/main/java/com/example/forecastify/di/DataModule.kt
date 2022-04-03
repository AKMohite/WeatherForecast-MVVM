package com.example.forecastify.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.forecastify.BuildConfig
import com.example.forecastify.data.API_BASE_URL
import com.example.forecastify.data.API_KEY
import com.example.forecastify.data.WeatherAPI
import com.example.forecastify.data.db.WeatherDB
import com.example.forecastify.data.network.ConnectivityInterceptorImpl
import com.example.forecastify.data.network.MockInterceptor
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val WEATHER_DB: String = "forecast.db"

    @Singleton
    @Provides
    fun provideWeatherDB(
        app: Application
    ) = Room.databaseBuilder(app, WeatherDB::class.java, WEATHER_DB)
        .fallbackToDestructiveMigration() // TODO room migration
        .build()


    @Singleton
    @Provides
    @Named("LoggingInterceptor")
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

    @Singleton
    @Provides
    @Named("MockInterceptor")
    fun provideMockInterceptor(): Interceptor = MockInterceptor()

    @Singleton
    @Provides
    @Named("ConnectivityInterceptor")
    fun provideConnectivityInterceptor(
        @ApplicationContext context: Context
    ): Interceptor = ConnectivityInterceptorImpl(context)

    @Singleton
    @Provides
    fun provideCallFactory(
        @Named("LoggingInterceptor") loggingInterceptor: Interceptor,
        @Named("ConnectivityInterceptor") connectivityInterceptor: Interceptor,
        @Named("MockInterceptor") mockInterceptor: Interceptor
    ): Call.Factory {
        return OkHttpClient.Builder()
            .addInterceptor(connectivityInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val url = original.url.newBuilder()
                    .addQueryParameter("access_key", API_KEY).build()
                val request = original.newBuilder().url(url)
                chain.proceed(request.build())
            }
            .addInterceptor(loggingInterceptor)
            .addInterceptor(mockInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Singleton
    @Provides
    fun provideMoshiConverter(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        callFactory: Call.Factory
    ): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .callFactory(callFactory)
        .addConverterFactory(moshiConverterFactory)

    @Singleton
    @Provides
    fun provideWeatherAPI(retrofit: Retrofit.Builder): WeatherAPI = retrofit
        .build()
        .create(WeatherAPI::class.java)

    @Provides
    fun provideLocationProvider(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

}