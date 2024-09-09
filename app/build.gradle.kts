import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.9.21"
    id("kotlin-parcelize") // @Parcelize
}

val properties: Properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
val serverUrl = properties.getProperty("SERVER_URL")
val kakaoKey = properties.getProperty("KAKAO_KEY")

android {
    namespace = "com.example.daejangjung2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.daejangjung2"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "SERVER_URL", serverUrl)
        buildConfigField("String", "KAKAO_KEY", kakaoKey)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            buildConfigField("Boolean", "DEBUG_MODE", "false")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            buildConfigField("Boolean", "DEBUG_MODE", "true")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    dataBinding {
        enable = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.databinding:databinding-runtime:8.2.1")
    implementation("androidx.activity:activity:1.9.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // lifecycle
    implementation("androidx.activity:activity-ktx:1.7.2") // by viewModels()
    implementation("androidx.fragment:fragment-ktx:1.6.2") // by activityViewModels()
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    // splash screen api
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")

    // app update manager
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    // lottie 애니메이션
    implementation("com.airbnb.android:lottie:6.1.0")

    // 이미지 처리
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // 레트로핏
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.14.9")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // 직렬화 / 역직렬화 라이브러리
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    // dataStore
    implementation("androidx.datastore:datastore:1.0.0")
    implementation(kotlin("reflect"))

    // Timber
    implementation("com.jakewharton.timber:timber:4.7.1")

    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.retrofit2:converter-gson:2.6.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    implementation("com.kakao.maps.open:android:2.11.9")

    //Glide
    implementation("androidx.viewpager2:viewpager2:1.0.0-alpha04")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")
    implementation("androidx.cardview:cardview:1.0.0")

}