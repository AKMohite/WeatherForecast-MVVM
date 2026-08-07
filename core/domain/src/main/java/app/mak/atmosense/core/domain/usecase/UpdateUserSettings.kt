package app.mak.atmosense.core.domain.usecase

import app.mak.atmosense.core.common.model.UserSettings
import app.mak.atmosense.core.domain.repository.SettingsRepository
import dev.zacsweers.metro.Inject

@Inject
class UpdateUserSettings(
  private val settingsRepository: SettingsRepository
) {
  suspend operator fun invoke(settings: UserSettings) {
    settingsRepository.updateUserSettings(settings)
  }
}
