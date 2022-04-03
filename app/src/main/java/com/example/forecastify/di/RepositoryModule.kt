package com.example.forecastify.di

import com.example.forecastify.data.repository.ForecastRepository
import com.example.forecastify.data.repository.ForecastRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindForecastRepository(repository: ForecastRepositoryImpl): ForecastRepository
}