import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.maven.publish)
    signing
}

group = "io.github.scarlet-pan"
version = "1.1.0-beta"

val xcfName = "KmpLogger"

kotlin {

    applyDefaultHierarchyTemplate()

    jvm {
        compilations.configureEach {
            compilerOptions.configure {
                jvmTarget.set(JvmTarget.JVM_1_8)
            }
        }
    }

    androidLibrary {
        namespace = "dev.scarlet.logger"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava() // enable java compilation support
        withAndroidTestOnJvmBuilder {

        }.configure {

        }
        withAndroidTestOnDeviceBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        compilations.configureEach {
            compilerOptions.configure {
                jvmTarget.set(JvmTarget.JVM_1_8)
            }
        }
    }

    iosX64 {
        binaries {
            framework {
                baseName = xcfName
                isStatic = false
            }
        }
    }
    iosArm64 {
        binaries {
            framework {
                baseName = xcfName
                isStatic = false
            }
        }
    }
    iosSimulatorArm64{
        binaries {
            framework {
                baseName = xcfName
                isStatic = false
            }
        }
    }

    js(IR) {
        browser {
            testTask {
                enabled = false // Disabled: Karma incompatible in Kotlin/JS 1.9+; no DOM needed.
            }
        }
        nodejs()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {

            }
        }

        getByName("androidTestOnJvm") {
            dependencies {
                implementation(libs.robolectric)
            }
        }

        getByName("androidTestOnDevice") {
            dependencies {
                implementation(libs.androidx.test.runner)
                implementation(libs.androidx.test.core)
                implementation(libs.androidx.test.junit)
                implementation(libs.androidx.espresso.core)
            }
        }

        iosMain {
            dependencies {

            }
        }

        iosTest {
            dependencies {

            }
        }

        jsTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlin.test.js)
            }
        }
    }

}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "logger", version.toString())

    pom {
        name = "Logger"
        description = "A Kotlin Multiplatform logging library."
        inceptionYear = "2025"
        url = "https://github.com/scarlet-pan/logger"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "Scarlet-Pan"
                name = "Scarlet Pan"
                email = "scarletpan@qq.com"
            }
        }
        scm {
            url = "https://github.com/scarlet-pan/logger"
            connection = "scm:git:https://github.com/scarlet-pan/logger.git"
            developerConnection = "scm:git:ssh://git@github.com/scarlet-pan/logger.git"
        }
    }
}

signing {
    useInMemoryPgpKeys(
        providers.gradleProperty("signingInMemoryKeyId").orNull,
        providers.gradleProperty("signingInMemoryKey").orNull,
        providers.gradleProperty("signingInMemoryKeyPassword").orNull
    )
    sign(publishing.publications)
}


tasks.register<Exec>("mergeSimulatorFrameworks") {
    onlyIf { org.gradle.internal.os.OperatingSystem.current().isMacOsX }

    val universalDir = layout.buildDirectory.dir("tmp/simulator-universal").get().asFile
    val universalFramework = File(universalDir, "$xcfName.framework")

    inputs.files(
        layout.buildDirectory.file("bin/iosX64/releaseFramework/$xcfName.framework/$xcfName"),
        layout.buildDirectory.file("bin/iosSimulatorArm64/releaseFramework/$xcfName.framework/$xcfName")
    )
    outputs.dir(universalDir)

    doFirst {
        universalDir.mkdirs()
        val arm64SimFramework = layout.buildDirectory.dir("bin/iosSimulatorArm64/releaseFramework").get().asFile.resolve("$xcfName.framework")
        arm64SimFramework.copyRecursively(universalFramework, overwrite = true)
    }

    commandLine = listOf(
        "lipo", "-create",
        layout.buildDirectory.file("bin/iosX64/releaseFramework/$xcfName.framework/$xcfName").get().asFile.absolutePath,
        layout.buildDirectory.file("bin/iosSimulatorArm64/releaseFramework/$xcfName.framework/$xcfName").get().asFile.absolutePath,
        "-output", File(universalFramework, xcfName).absolutePath
    )
}

tasks.register<Exec>("buildXCFramework") {
    onlyIf { org.gradle.internal.os.OperatingSystem.current().isMacOsX }

    val outputXcframework = File(project.rootDir, "$xcfName.xcframework")
    val podspecFile = File(project.rootDir, "$xcfName.podspec")

    inputs.files(
        layout.buildDirectory.file("bin/iosArm64/releaseFramework/$xcfName.framework"),
        layout.buildDirectory.dir("tmp/simulator-universal").get().asFile.resolve("$xcfName.framework")
    )
    outputs.files(outputXcframework, podspecFile)

    dependsOn(
        "linkReleaseFrameworkIosArm64",
        "linkReleaseFrameworkIosX64",
        "linkReleaseFrameworkIosSimulatorArm64",
        "mergeSimulatorFrameworks"
    )

    doFirst {
        if (outputXcframework.exists()) {
            println("🗑️ Deleting existing ${outputXcframework.name}")
            outputXcframework.deleteRecursively()
        }
    }

    commandLine = listOf(
        "xcodebuild", "-create-xcframework",
        "-output", outputXcframework.absolutePath,
        "-framework", layout.buildDirectory.file("bin/iosArm64/releaseFramework/$xcfName.framework").get().asFile.absolutePath,
        "-framework", layout.buildDirectory.dir("tmp/simulator-universal").get().asFile.resolve("$xcfName.framework").absolutePath
    )

    doLast {
        if (!outputXcframework.exists()) {
            throw RuntimeException("❌ xcframework not created at: ${outputXcframework.absolutePath}")
        }

        podspecFile.writeText("""
            Pod::Spec.new do |s|
              s.name         = '$xcfName'
              s.version      = '$version'
              s.summary      = 'Kotlin Multiplatform logging library for iOS'
              s.homepage     = 'https://github.com/Scarlet-Pan/logger'
              s.license      = { :type => 'MIT', :file => 'LICENSE' }
              s.authors      = { 'Scarlet Pan' => 'scarletpan@qq.com' }
              s.source       = { :git => 'https://github.com/Scarlet-Pan/logger.git', :tag => '$version' }
              s.vendored_frameworks = '$xcfName.xcframework'
              s.ios.deployment_target = '12.0'
              s.swift_version = '5.0'
            end
        """.trimIndent())

        println("✅ Success: ${outputXcframework.name} and ${podspecFile.name} generated (version: $version)")
    }
}