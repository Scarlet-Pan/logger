import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
    signing
}

group = "io.github.scarlet-pan"
version = "1.1.0"

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

    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
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

        getByName("androidUnitTest") {
            dependencies {
                implementation(libs.robolectric)
            }
        }

        getByName("androidInstrumentedTest") {
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

android {
    namespace = "dev.scarlet.logger"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
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

    val xcfName = xcfName
    val universalDir = layout.buildDirectory.dir("tmp/simulator-universal").get().asFile
    val universalFramework = File(universalDir, "${xcfName}.framework")

    dependsOn(
        tasks.named("linkReleaseFrameworkIosX64"),
        tasks.named("linkReleaseFrameworkIosSimulatorArm64")
    )

    inputs.file(layout.buildDirectory.file("bin/iosX64/releaseFramework/${xcfName}.framework/${xcfName}"))
    inputs.file(layout.buildDirectory.file("bin/iosSimulatorArm64/releaseFramework/${xcfName}.framework/${xcfName}"))

    outputs.dir(universalDir)

    doFirst {
        universalDir.mkdirs()
        val appleSiliconFramework = layout.buildDirectory.dir("bin/iosSimulatorArm64/releaseFramework").get().asFile.resolve("${xcfName}.framework")
        appleSiliconFramework.copyRecursively(universalFramework, overwrite = true)
    }

    commandLine = listOf(
        "lipo", "-create",
        layout.buildDirectory.file("bin/iosX64/releaseFramework/${xcfName}.framework/${xcfName}").get().asFile.absolutePath,
        layout.buildDirectory.file("bin/iosSimulatorArm64/releaseFramework/${xcfName}.framework/${xcfName}").get().asFile.absolutePath,
        "-output", File(universalFramework, xcfName).absolutePath
    )
}

tasks.register<Exec>("buildXCFramework") {
    onlyIf { org.gradle.internal.os.OperatingSystem.current().isMacOsX }

    val rootDir = project.rootDir
    val xcfName = xcfName
    val output = File(rootDir, "${xcfName}.xcframework")
    val version = project.version.toString()

    dependsOn(
        "linkReleaseFrameworkIosArm64",
        "linkReleaseFrameworkIosX64",
        "linkReleaseFrameworkIosSimulatorArm64",
        "mergeSimulatorFrameworks"
    )

    outputs.dir(output)
    outputs.file(File(rootDir, "${xcfName}.podspec"))

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
        "-framework", layout.buildDirectory.dir("tmp/simulator-universal").get().asFile.resolve("${xcfName}.framework").absolutePath
    )

    doLast {
        if (!output.exists()) throw RuntimeException("xcframework not created!")

        File(rootDir, "${xcfName}.podspec").writeText("""
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

        println("✅ Success: $xcfName.xcframework and $xcfName.podspec generated (version: $version)")
    }
}