package app.mak.nimbus.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.mak.nimbus.core.data.local.converter.WeatherTypeConverters
import app.mak.nimbus.core.data.local.dao.CityDao
import app.mak.nimbus.core.data.local.dao.WeatherDao
import app.mak.nimbus.core.data.local.entity.CityEntity
import app.mak.nimbus.core.data.local.entity.CurrentWeatherEntity
import app.mak.nimbus.core.data.local.entity.DailyForecastEntity
import app.mak.nimbus.core.data.local.entity.HourlyForecastEntity
import app.mak.nimbus.core.data.local.entity.WeatherAlertEntity

@Database(
    entities = [
        CityEntity::class,
        CurrentWeatherEntity::class,
        HourlyForecastEntity::class,
        DailyForecastEntity::class,
        WeatherAlertEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(WeatherTypeConverters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun weatherDao(): WeatherDao
}