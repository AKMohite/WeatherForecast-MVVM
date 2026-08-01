package app.mak.atmosense.core.database.dao.api

import app.mak.atmosense.core.database.dao.CityEntity

interface CityDAO {
  suspend fun insert(city: CityEntity)
  suspend fun getById(id: Long): CityEntity?
  suspend fun getAll(): List<CityEntity>
  suspend fun delete(id: Long)
  suspend fun deleteAll()
}
