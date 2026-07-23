package app.mak.nimbus.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
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
abstract class RoomWeatherDatabase : RoomDatabase(), WeatherDatabase {

    override suspend fun inTransaction(block: () -> Unit) {
        this.withTransaction {
            block()
        }
    }

}

interface WeatherDatabase {
    fun cityDao(): CityDao
    fun weatherDao(): WeatherDao

    suspend fun inTransaction(block: () -> Unit)
}
