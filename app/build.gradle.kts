plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //Parcelize
    id("kotlin-parcelize")
    //Maps
    id("com.google.gms.google-services")

    id("kotlin-kapt")
}

android {
    namespace = "com.batool.josequaltask"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.batool.josequaltask"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    dataBinding {
        enable = true;
    }
    viewBinding {
        enable = true;
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.activity:activity-ktx:1.8.1")
    //maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    //location helper
    implementation("com.github.BirjuVachhani:locus-android:4.1.0")

    //Location
    implementation("com.google.android.gms:play-services-location:21.0.1")

    //permission helper
    implementation("com.karumi:dexter:6.2.3")

    //coil
    implementation("io.coil-kt:coil:2.5.0")

    //Kml
    implementation("com.google.maps.android:android-maps-utils:2.0.3")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")



}