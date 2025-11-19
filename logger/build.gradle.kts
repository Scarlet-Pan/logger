import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.maven.publish)
    signing
}

group = "io.github.scarlet-pan"
version = "1.1.0-alpha05"

val xcfName = "loggerKit"

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

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js(IR) {
        browser {
            testTask {
                enabled = false // Disabled: Karma incompatible in Kotlin/JS 1.9+; no DOM needed.
            }
        }
        nodejs()
        binaries.executable()
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries {
            framework {
                baseName = xcfName
                isStatic = false
            }
        }
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

tasks.register<Exec>("buildXCFramework") {
    onlyIf { org.gradle.internal.os.OperatingSystem.current().isMacOsX }

    val rootDir = project.rootDir
    val output = File(rootDir, "${xcfName}.xcframework")
    val version = project.version.toString()

    // Ensure frameworks are built
    dependsOn("linkReleaseFrameworkIosArm64", "linkReleaseFrameworkIosSimulatorArm64")

    doFirst {
        if (output.exists()) {
            println("🗑️ Deleting existing $output")
            output.deleteRecursively()
        }
    }

    commandLine = listOf(
        "xcodebuild", "-create-xcframework",
        "-output", output.absolutePath,
        "-framework", layout.buildDirectory.file("bin/iosArm64/releaseFramework/${xcfName}.framework").get().asFile.absolutePath,
        "-framework", layout.buildDirectory.file("bin/iosSimulatorArm64/releaseFramework/${xcfName}.framework").get().asFile.absolutePath
    )

    doLast {
        val deviceFramework = layout.buildDirectory.file("bin/iosArm64/releaseFramework/${xcfName}.framework").get().asFile
        val simFramework = layout.buildDirectory.file("bin/iosSimulatorArm64/releaseFramework/${xcfName}.framework").get().asFile

        if (!deviceFramework.exists()) throw RuntimeException("Device framework missing!")
        if (!simFramework.exists()) throw RuntimeException("Simulator framework missing!")

        // ✅ Generate CORRECT .podspec with pure version
        File(rootDir, "${xcfName}.podspec").writeText("""
            Pod::Spec.new do |s|
              s.name         = '$xcfName'
              s.version      = '$version'
              s.summary      = 'Kotlin Multiplatform logging library for iOS'
              s.homepage     = 'https://github.com/Scarlet-Pan/logger'
              s.license      = { :type => 'MIT', :file => 'LICENSE' }
              s.authors      = { 'Scarlet Pan' => 'scarletpan@qq.com' }
              s.source       = { :git => 'https://github.com/Scarlet-Pan/logger.git', :tag => '$version' }
              s.source_files = 'dummy.swift'
              s.vendored_frameworks = '$xcfName.xcframework'
              s.ios.deployment_target = '12.0'
              s.swift_version = '5.0'
            end
        """.trimIndent())

        println("✅ Success: $xcfName.xcframework and $xcfName.podspec generated (version: $version)")
    }
}