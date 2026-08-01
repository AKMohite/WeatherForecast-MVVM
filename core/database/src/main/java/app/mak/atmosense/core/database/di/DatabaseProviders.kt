package app.mak.atmosense.core.database.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.mak.atmosense.core.database.AtmosenseDatabase
import app.mak.atmosense.core.database.adapter.InstantAdapter
import app.mak.atmosense.core.database.dao.CityEntity
import app.mak.atmosense.core.database.dao.CurrentWeatherEntity
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface DatabaseProviders {

  @SingleIn(AppScope::class)
  @Provides
  fun provideDriver(
    context: Context
  ): SqlDriver = AndroidSqliteDriver(
    schema = AtmosenseDatabase.Schema,
    context = context,
    name = "atmosense.db"
  )

  @SingleIn(AppScope::class)
  @Provides
  fun provideDatabase(driver: SqlDriver): AtmosenseDatabase {
    return AtmosenseDatabase(
      driver = driver,
      cityEntityAdapter = CityEntity.Adapter(
        added_atAdapter = InstantAdapter
      ),
      currentWeatherEntityAdapter = CurrentWeatherEntity.Adapter(
        fetched_atAdapter = InstantAdapter
      )
    )
  }
}
