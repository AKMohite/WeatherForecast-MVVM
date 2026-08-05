package app.mak.atmosense.core.data.testing

import app.mak.atmosense.core.database.dao.SyncEntity
import app.mak.atmosense.core.database.dao.api.SyncDAO

class FakeSyncDAO : SyncDAO {
  val syncStatus = mutableMapOf<Pair<Long, String>, SyncEntity>()

  override fun insert(syncEntity: SyncEntity) {
    syncStatus[Pair(syncEntity.city_id, syncEntity.sync_type)] = syncEntity
  }

  override fun getSyncStatus(cityId: Long, syncType: String): SyncEntity? {
    return syncStatus[Pair(cityId, syncType)]
  }

  override fun deleteByCityId(cityId: Long) {
    val keysToRemove = syncStatus.keys.filter { it.first == cityId }
    keysToRemove.forEach { syncStatus.remove(it) }
  }

  override fun deleteAll() {
    syncStatus.clear()
  }
}
