plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
}

android {
    namespace = "com.ashuthosh.calendar"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ashuthosh.calendar"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    tasks.withType(Test::class) {
        useJUnitPlatform()
    }
}

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.viewmodel)
    implementation(libs.viewmodel.ktx)

    implementation(libs.google.material)
    implementation(libs.kotlin.coroutine)

    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    testImplementation(libs.junit5)
    testImplementation(libs.kotlin.coroutine.test)
    testImplementation(libs.mockk)
}