package app.mak.atmosense.core.domain.repository

import app.mak.atmosense.core.common.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
  fun getUserSettings(): Flow<UserSettings>
  suspend fun updateUserSettings(settings: UserSettings)
}
