package app.mak.atmosense.core.data.testing

import app.mak.atmosense.core.database.dao.api.DatabaseTransaction

class FakeDatabaseTransaction : DatabaseTransaction {
  override suspend fun <T> invoke(block: () -> T) {
    block()
  }
}
