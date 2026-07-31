package app.mak.atmosense.core.location.di

import android.content.Context
import app.mak.atmosense.core.location.FuseLocationService
import app.mak.atmosense.core.location.LocationService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface LocationProvider {

  @SingleIn(AppScope::class)
  @Provides
  fun provideFusedLocationProviderClient(
    context: Context,
  ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

  @SingleIn(AppScope::class)
  @Provides
  fun provideLocationProvider(client: FusedLocationProviderClient): LocationService =
    FuseLocationService(client)

}
