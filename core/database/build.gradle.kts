plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.metro)
  alias(libs.plugins.sqldelight)
}

android {
  namespace = "app.mak.atmosense.core.database"
  compileSdk = libs.versions.compileSDK.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSDK.get().toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

}

dependencies {
  implementation(libs.kotlinx.datetime)
  implementation(libs.android.sql)
  implementation(libs.sql.coroutines.extensions)
  implementation(libs.kotlinx.coroutines.core)
}

sqldelight {
  databases {
    register("AtmosenseDatabase") {
      packageName.set("app.mak.atmosense.core.database")
    }
  }
}
