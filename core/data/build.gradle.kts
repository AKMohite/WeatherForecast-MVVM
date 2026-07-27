plugins {
  alias(libs.plugins.android.library)
}

android {
  namespace = "app.mak.atmosense.core.data"
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
  implementation(project(":core:domain"))
  implementation(project(":core:network"))
  implementation(project(":core:database"))
}
