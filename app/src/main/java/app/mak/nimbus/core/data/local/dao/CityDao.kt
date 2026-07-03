package app.mak.nimbus.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import app.mak.nimbus.core.data.local.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY sortOrder ASC")
    fun getCities(): Flow<List<CityEntity>>

    @Upsert
    suspend fun upsertCity(city: CityEntity)

    @Delete
    suspend fun deleteCity(city: CityEntity)

    @Query("SELECT * FROM cities WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultCity(): CityEntity?
}