package com.example.forecastify.di

import com.example.forecastify.data.provider.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface ProvideModule {

    @Binds
    fun bindLocationProvider(provider: LocationProviderImpl): LocationProvider

    @Binds
    fun bindThemeProvider(provider: ThemeProviderImpl): ThemeProvider

    @Binds
    fun bindUnitProvider(provider: UnitProviderImpl): UnitProvider

}