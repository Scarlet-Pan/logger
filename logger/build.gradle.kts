import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.maven.publish)
    signing
}

group = "io.github.scarlet-pan"
version = "1.1.0-SNAPSHOT"

val xcfName = "loggerKit"

kotlin {

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
        binaries.framework {
            baseName = xcfName
            isStatic = true
        }
    }
    iosArm64 {
        binaries.framework {
            baseName = xcfName
            isStatic = true
        }
    }
    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
            isStatic = true
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
    val outputDir = layout.buildDirectory.dir("xcframework").get().asFile
    val frameworkOutput = File(outputDir, "$xcfName.xcframework")

    // 清理旧产物
    doFirst {
        if (outputDir.exists()) outputDir.deleteRecursively()
        outputDir.mkdirs()
    }

    // 构建命令
    commandLine = listOf(
        "xcodebuild",
        "-create-xcframework",
        "-output", frameworkOutput.absolutePath,
        "-framework", project.file("build/bin/iosArm64/releaseFramework/$xcfName.framework").absolutePath,
        "-framework", project.file("build/bin/iosSimulatorArm64/releaseFramework/$xcfName.framework").absolutePath,
        "-framework", project.file("build/bin/iosX64/releaseFramework/$xcfName.framework").absolutePath
    )

    // 依赖各个平台的 release framework 构建任务
    dependsOn(
        "linkReleaseFrameworkIosArm64",
        "linkReleaseFrameworkIosSimulatorArm64",
        "linkReleaseFrameworkIosX64"
    )

    doLast {
        println("✅ .xcframework built at: ${frameworkOutput.absolutePath}")
        println("📦 You can now drag it into Xcode or distribute it!")
    }
}