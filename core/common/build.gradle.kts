plugins {
  alias(libs.plugins.android.library)
}

android {
  namespace = "app.mak.atmosense.core.common"
  compileSdk = libs.versions.compileSDK.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSDK.get().toInt()
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }
}

dependencies {
  implementation(libs.javax.inject)
  implementation(libs.kotlinx.datetime)
}
