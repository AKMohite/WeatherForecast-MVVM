package app.mak.atmosense.core.domain.usecase

import app.mak.atmosense.core.common.model.UserSettings
import app.mak.atmosense.core.domain.repository.SettingsRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

@Inject
class ObserveUserSettings(
  private val settingsRepository: SettingsRepository
) {
  operator fun invoke(): Flow<UserSettings> = settingsRepository.getUserSettings()
}
