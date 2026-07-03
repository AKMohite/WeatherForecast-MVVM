package app.mak.nimbus.core.di

import android.content.Context
import androidx.room.Room
import app.mak.nimbus.core.data.local.WeatherDatabase
import app.mak.nimbus.core.data.local.dao.CityDao
import app.mak.nimbus.core.data.local.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(
        @ApplicationContext context: Context
    ): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather.db"
        ).build()
    }

    @Provides
    fun provideCityDao(database: WeatherDatabase): CityDao {
        return database.cityDao()
    }

    @Provides
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao {
        return database.weatherDao()
    }
}