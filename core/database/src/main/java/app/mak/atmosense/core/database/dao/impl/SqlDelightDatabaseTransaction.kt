package app.mak.atmosense.core.database.dao.impl

import app.mak.atmosense.core.database.AtmosenseDatabase
import app.mak.atmosense.core.database.dao.api.DatabaseTransaction
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class SqlDelightDatabaseTransaction(
  private val db: AtmosenseDatabase
) : DatabaseTransaction {
  override suspend fun <T> invoke(block: () -> T) {
    return db.transactionWithResult {
      block()
    }
  }
}
