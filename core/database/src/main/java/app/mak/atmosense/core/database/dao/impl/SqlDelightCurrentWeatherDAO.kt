package app.mak.atmosense.core.database.dao.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneNotNull
import app.mak.atmosense.core.database.AtmosenseDatabase
import app.mak.atmosense.core.database.dao.CurrentWeatherEntity
import app.mak.atmosense.core.database.dao.GetCitiesWeather
import app.mak.atmosense.core.database.dao.api.CurrentWeatherDAO
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

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

  override fun observeCitiesWeather(): Flow<List<GetCitiesWeather>> {
    return query.getCitiesWeather()
      .asFlow()
      .mapToList(Dispatchers.IO)
  }

  override fun observeCityCurrentWeather(cityId: Long): Flow<CurrentWeatherEntity?> {
    return query.getByCityId(cityId)
      .asFlow()
      .mapToOneNotNull(Dispatchers.IO)
  }
}
