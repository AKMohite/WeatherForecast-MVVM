package app.mak.atmosense.core.database.dao.api

import app.mak.atmosense.core.database.dao.SyncEntity

interface SyncDAO {
  fun insert(syncEntity: SyncEntity)
  fun getSyncStatus(cityId: Long, syncType: String): SyncEntity?
  fun deleteByCityId(cityId: Long)
  fun deleteAll()
}
