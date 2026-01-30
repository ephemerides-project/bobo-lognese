plugins {
    java
    war

    alias(libs.plugins.lombok)
}

group = "com.lescours"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.bundles.jakarta.apis)

    implementation(libs.bundles.hibernate)

    implementation(libs.bundles.jjwt)

    implementation(libs.bouncycastle)
}
