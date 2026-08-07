plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.metro)
  alias(libs.plugins.protobuf)
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
  implementation(project(":core:common"))
  implementation(project(":core:domain"))
  implementation(project(":core:network"))
  implementation(project(":core:database"))
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.javax.inject)
  implementation(libs.androidx.datastore)
  implementation(libs.protobuf.javalite)
  implementation(libs.protobuf.kotlin.lite)

  testImplementation(libs.junit)
  testImplementation(libs.coroutines.test)
  testImplementation(testFixtures(project(":core:network")))
}

protobuf {
  protoc {
    artifact = libs.protobuf.protoc.get().toString()
  }
  generateProtoTasks {
    all().forEach { task ->
      task.builtins {
        register("java") {
          option("lite")
        }
        register("kotlin") {
          option("lite")
        }
      }
    }
  }
}
