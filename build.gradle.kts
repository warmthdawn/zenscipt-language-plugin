plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0-RC"
    id("org.jetbrains.intellij") version "1.14.2"
}

group = "com.warmthdawn"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.1.3")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(
            "java",
            "org.jetbrains.kotlin",
            "com.jetbrains.hackathon.indices.viewer:1.23"
    ))
}

sourceSets {
    main {
        java {
            srcDirs("src/main/gen")
        }
    }
}


tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
    }

    patchPluginXml {
        sinceBuild.set("213")
//        untilBuild.set("223.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
