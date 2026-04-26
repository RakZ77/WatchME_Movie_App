import com.android.build.gradle.ProguardFiles.getDefaultProguardFile
import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "kh.edu.rupp.watchme"
    compileSdk = 36

    defaultConfig {
        applicationId = "kh.edu.rupp.watchme"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SUPABASE_ANON_KEY", "\"${localProperties["SUPABASE_ANON_KEY"]}\"")
        buildConfigField("String", "TMDB_API_KEY", "\"${localProperties["TMDB_API_KEY"]}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.10.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.10.0")
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-rxjava3:2.8.4")
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.gson)
    implementation(libs.picasso)
    annotationProcessor("androidx.room:room-compiler:2.8.4")
    implementation("androidx.datastore:datastore-preferences-rxjava3:1.2.0")
    implementation("io.reactivex.rxjava3:rxjava:3.1.6")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("com.google.android.material:material:1.11.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
