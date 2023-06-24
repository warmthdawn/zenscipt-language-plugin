plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    id("org.jetbrains.intellij") version "1.8.0"
    id("org.jetbrains.grammarkit") version "2021.2.2"
}

group = "com.warmthdawn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.2.4")
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
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
        kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
    }

    patchPluginXml {
        sinceBuild.set("213")
        untilBuild.set("223.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    generateLexer {
        source.set("src/main/grammar/ZenScript.flex")
        targetDir.set("src/main/gen/com/warmthdawn/zenscript/lexer")
        targetClass.set("ZenScriptLexer")
        purgeOldFiles.set(true)
    }

    generateParser {
        source.set("src/main/grammar/ZenScript.bnf")
        pathToParser.set("com/warmthdawn/zenscript/parser/ZenScriptParser.java")
        pathToPsiRoot.set("com/warmthdawn/zenscript/psi")
        purgeOldFiles.set(true)
        targetRoot.set("src/main/gen")
    }
}
