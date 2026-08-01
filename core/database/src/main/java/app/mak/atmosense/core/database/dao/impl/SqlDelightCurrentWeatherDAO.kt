package app.mak.atmosense.core.database.dao.impl

import app.mak.atmosense.core.database.AtmosenseDatabase
import app.mak.atmosense.core.database.dao.CurrentWeatherEntity
import app.mak.atmosense.core.database.dao.api.CurrentWeatherDAO
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@ContributesBinding(scope = AppScope::class)
@SingleIn(AppScope::class)
class SqlDelightCurrentWeatherDAO(
  db: AtmosenseDatabase
) : CurrentWeatherDAO {

  private val query = db.current_weatherQueries

  override fun insert(currentWeather: CurrentWeatherEntity) {
    query.insert(currentWeather)
  }

  override fun getByCityId(cityId: Long): CurrentWeatherEntity? {
    return query.getByCityId(cityId)
      .executeAsOneOrNull()
  }

  override fun deleteByCityId(cityId: Long) {
    query.deleteByCityId(cityId)
  }

  override fun deleteAll() {
    query.deleteAll()
  }
}
