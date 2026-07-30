plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.metro)
}

android {
  namespace = "app.mak.atmosense.core.network"
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
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.cio)
  implementation(libs.ktor.client.android)
  implementation(libs.ktor.client.content.negotiation)
  implementation(libs.ktor.serialization.kotlinx.json)
}
