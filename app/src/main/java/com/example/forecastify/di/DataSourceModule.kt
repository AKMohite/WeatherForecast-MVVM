package com.example.forecastify.di

import com.example.forecastify.data.network.WeatherNetworkDataSource
import com.example.forecastify.data.network.WeatherNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface DataSourceModule {

    @Binds
    fun bindRemoteDataSource(dataSource: WeatherNetworkDataSourceImpl): WeatherNetworkDataSource

//    todo add local data source
    /*@Binds
    fun bindLocalDataSource(dataSource: WeatherLocalDataSourceImpl): WeatherLocalDataSource*/
}