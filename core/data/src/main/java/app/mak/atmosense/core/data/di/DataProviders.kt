package app.mak.atmosense.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import app.mak.atmosense.core.data.UserSettingsProto
import app.mak.atmosense.core.data.datastore.UserSettingsSerializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@ContributesTo(AppScope::class)
interface DataProviders {
  @SingleIn(AppScope::class)
  @Provides
  fun provideUserSettingsDataStore(
    context: Context,
    userSettingsSerializer: UserSettingsSerializer
  ): DataStore<UserSettingsProto> =
    DataStoreFactory.create(
      serializer = userSettingsSerializer,
      scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    ) {
      context.dataStoreFile("user_settings.pb")
    }
}
