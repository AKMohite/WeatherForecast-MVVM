package app.mak.atmosense.core.database.dao.impl

import app.mak.atmosense.core.database.AtmosenseDatabase
import app.mak.atmosense.core.database.dao.CityEntity
import app.mak.atmosense.core.database.dao.api.CityDAO
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(scope = AppScope::class)
class SqlDelightCityDAO(
  db: AtmosenseDatabase
) : CityDAO {
  private val query = db.cityQueries
  override fun insert(city: CityEntity) {
    query.insert(city)
  }

  override fun getById(id: Long): CityEntity? {
    return query.getById(id)
      .executeAsOneOrNull()
  }

  override fun getAll(): List<CityEntity> {
    return query.getAll()
      .executeAsList()
  }

  override fun delete(id: Long) {
    query.delete(id)
  }

  override fun deleteAll() {
    query.deleteAll()
  }
}
