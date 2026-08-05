plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.metro)
  alias(libs.plugins.kotlinx.serialization)
}

android {
  namespace = "app.mak.atmosense.core.network"
  compileSdk = libs.versions.compileSDK.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSDK.get().toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  testFixtures {
    enable = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

}

dependencies {
  implementation(project(":core:common"))
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.cio)
  implementation(libs.ktor.client.android)
  implementation(libs.ktor.client.content.negotiation)
  implementation(libs.ktor.serialization.kotlinx.json)

  testImplementation(libs.junit)
  testImplementation(libs.coroutines.test)
  testImplementation(libs.ktor.client.mock)
}
