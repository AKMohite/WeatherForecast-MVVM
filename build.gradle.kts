// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        jcenter()
        
    }
    dependencies {
        classpath(Libs.gradlePlugin)
        classpath(Libs.kotlinGradle)
        classpath(Libs.navigationSafeArgs)
        classpath(Libs.hiltGradlePlugin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        
    }
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}
