package app.mak.nimbus.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val id: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String?,
    val state: String?,
    val isDefault: Boolean = false,
    val sortOrder: Int = 0
)