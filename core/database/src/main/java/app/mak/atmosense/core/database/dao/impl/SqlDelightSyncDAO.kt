package app.mak.atmosense.core.database.dao.impl

import app.mak.atmosense.core.database.AtmosenseDatabase
import app.mak.atmosense.core.database.dao.SyncEntity
import app.mak.atmosense.core.database.dao.api.SyncDAO
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@ContributesBinding(scope = AppScope::class)
@SingleIn(AppScope::class)
class SqlDelightSyncDAO(
  db: AtmosenseDatabase
) : SyncDAO {

  private val query = db.syncQueries

  override fun insert(syncEntity: SyncEntity) {
    query.insert(
      city_id = syncEntity.city_id,
      sync_type = syncEntity.sync_type,
      last_attempt_at = syncEntity.last_attempt_at,
      last_success_at = syncEntity.last_success_at
    )
  }

  override fun getSyncStatus(
    cityId: Long,
    syncType: String
  ): SyncEntity? {
    return query.getSyncStatus(
      cityId = cityId,
      syncType = syncType
    ).executeAsOneOrNull()
  }

  override fun deleteByCityId(cityId: Long) {
    query.deleteByCityId(cityId)
  }

  override fun deleteAll() {
    query.deleteAll()
  }


}
