package app.mak.atmosense.core.database.dao.api

import app.mak.atmosense.core.database.dao.CityEntity

interface CityDAO {
  fun insert(city: CityEntity)
  fun getById(id: Long): CityEntity?
  fun getAll(): List<CityEntity>
  fun delete(id: Long)
  fun deleteAll()
}
