plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.metro)
}

android {
  namespace = "app.mak.atmosense"
  compileSdk = libs.versions.compileSDK.get().toInt()

  defaultConfig {
    applicationId = "app.mak.atmosense"
    minSdk = libs.versions.minSDK.get().toInt()
    targetSdk = libs.versions.targetSDK.get().toInt()
    versionCode = 1
    versionName = "0.0.1"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    buildConfigField("String", "OWM_API_KEY", "\"" + propOrDef("OWM_API_KEY", "") + "\"")
  }

  buildTypes {
    release {
      optimization {
        enable = false
      }
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
}

ksp {
  arg("circuit.codegen.mode", "metro")
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:domain"))
  implementation(project(":core:network"))
  implementation(project(":core:database"))
  implementation(project(":core:data"))
  implementation(project(":core:location"))
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.circuit.foundation)
  implementation(libs.coil.compose)
  implementation(libs.coil.network.okhttp)
  api(libs.circuit.codegen.annotations)
  ksp(libs.circuit.codegen)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
}

fun <T : Any> propOrDef(
  propertyName: String,
  defaultValue: T,
): T {
  @Suppress("UNCHECKED_CAST")
  val propertyValue = project.findProperty(propertyName) as? T?
  return propertyValue ?: defaultValue
}
