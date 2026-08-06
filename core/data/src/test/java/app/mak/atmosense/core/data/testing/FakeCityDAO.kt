package app.mak.atmosense.core.data.testing

import app.mak.atmosense.core.database.dao.CityEntity
import app.mak.atmosense.core.database.dao.api.CityDAO

class FakeCityDAO : CityDAO {
  val cities = mutableMapOf<Long, CityEntity>()

  override fun insert(city: CityEntity) {
    cities[city.id] = city
  }

  override fun getById(id: Long): CityEntity? = cities[id]

  override fun getAll(): List<CityEntity> = cities.values.toList()

  override fun delete(id: Long) {
    cities.remove(id)
  }

  override fun deleteAll() {
    cities.clear()
  }
}
