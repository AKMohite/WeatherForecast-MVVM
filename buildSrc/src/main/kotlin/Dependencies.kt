object App {
    const val compileSdk = 30
    const val minSdk = 21
    const val targetSdk = 30
    const val versionCode = 1
    const val versionName = "0.0.1"
    const val applicationId = "com.example.forecastify"
}

object Versions {
    const val gradle = "7.1.0"
    const val kotlin = "1.5.10"
    const val appcompat = "1.3.1"
    const val navigation = "2.3.5"
    const val room = "2.3.0"
    const val coroutines = "1.5.1"
    const val lifecycle = "2.3.1"
    const val retrofit = "2.9.0"
    const val glide = "4.12.0"
    const val preference = "1.1.1"
    const val groupie = "2.1.0"
    const val hilt = "2.38.1"
    const val loggingInterceptor = "4.9.0"
    const val moshi = "1.11.0"

    /* test */
    const val junit = "4.13.2"
}

object Libs {
    const val gradlePlugin = "com.android.tools.build:gradle:${Versions.gradle}"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"


    const val androidxAppcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val androidxCore = "androidx.core:core-ktx:1.6.0"
    const val androidxLegacy = "androidx.legacy:legacy-support-v4:1.0.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.0"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val lifecycleExt = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val coroutinesAdapter = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"
    const val gson = "com.google.code.gson:gson:2.8.7"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val dateTimeSupport = "com.jakewharton.threetenabp:threetenabp:1.3.1"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val groupie = "com.xwray:groupie:${Versions.groupie}"
    const val groupieExt = "com.xwray:groupie-kotlin-android-extensions:${Versions.groupie}"
    const val preference = "androidx.preference:preference-ktx:${Versions.preference}"
    const val location = "com.google.android.gms:play-services-location:18.0.0"
    const val material = "com.google.android.material:material:1.4.0"
    const val dependencyUpdate = "com.github.ben-manes:gradle-versions-plugin:0.39.0"
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.loggingInterceptor}"
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"


}

object TestLibs {
    const val junit = "junit:junit:${Versions.junit}"
    const val junitExt = "androidx.test.ext:junit-ktx:1.1.3"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
}