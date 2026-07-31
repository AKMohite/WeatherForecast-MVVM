plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.metro)
}

android {
  namespace = "app.mak.atmosense.core.location"
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
  implementation(project(":core:common"))
  implementation(libs.play.services.location)
  implementation(libs.kotlinx.coroutines.play.services)
  testImplementation(libs.coroutines.test)
  testImplementation(libs.junit)
}
