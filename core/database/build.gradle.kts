plugins {
  alias(libs.plugins.android.library)
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

}

sqldelight {
  databases {
    register("AtmosenseDatabase") {
      packageName.set("app.mak.atmosense.core.database")
    }
  }
}
