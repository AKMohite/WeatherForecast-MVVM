package app.mak.atmosense.core.domain.testing

import app.mak.atmosense.core.common.model.UserSettings
import app.mak.atmosense.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSettingsRepository : SettingsRepository {
  private val settingsFlow = MutableStateFlow(UserSettings())

  override fun getUserSettings(): Flow<UserSettings> = settingsFlow

  override suspend fun updateUserSettings(settings: UserSettings) {
    settingsFlow.value = settings
  }
}
