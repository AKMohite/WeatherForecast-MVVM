plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.metro)
}
android {
  namespace = "app.mak.atmosense.core.domain"
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
  implementation(libs.kotlinx.coroutines.core)

  testFixturesImplementation(project(":core:common"))
  testFixturesImplementation(project(":core:domain"))
  testFixturesImplementation(libs.kotlinx.coroutines.core)
}
