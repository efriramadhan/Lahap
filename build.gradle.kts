plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    kotlin("android") version "1.9.10" apply false
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}