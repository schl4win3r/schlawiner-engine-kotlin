import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("multiplatform") version "2.3.10"
    id("maven-publish")
    id("io.gitlab.arturbosch.detekt") version ("1.23.8")
    id("org.jlleitschuh.gradle.ktlint") version ("12.1.1")
}

group = "io.schlawiner"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation(npm("uuid", "9.0.0"))
            }
        }
    }
}

val detektVersion = "1.23.8"
val detektConfigPath = "config/detekt/detekt.yml"

detekt {
    toolVersion = detektVersion
    config.setFrom(file(detektConfigPath))
    buildUponDefaultConfig = true

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
    }
}

tasks.withType<Detekt>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
        md.required.set(true)
    }
}
