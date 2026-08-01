package app.mak.atmosense.core.database.dao.api

interface DatabaseTransaction {
  suspend operator fun <T> invoke(block: () -> T)
}
