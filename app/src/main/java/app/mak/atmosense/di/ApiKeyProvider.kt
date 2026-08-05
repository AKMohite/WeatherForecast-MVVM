package app.mak.atmosense.di

import app.mak.atmosense.BuildConfig
import app.mak.atmosense.core.common.di.OWMApiKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface ApiKeyProvider {

  @OWMApiKey
  @SingleIn(AppScope::class)
  @Provides
  fun provideApiKey(): String = BuildConfig.OWM_API_KEY
}
