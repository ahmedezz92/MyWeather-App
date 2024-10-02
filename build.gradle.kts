// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.android.application") version "8.1.4" apply false
    kotlin("plugin.serialization") version "1.9.0" // match the Kotlin version in your project
    id ("org.jlleitschuh.gradle.ktlint") version "11.0.0" // For code style checks
    id ("io.gitlab.arturbosch.detekt")version "1.22.0"    // For code quality analysis
    id ("jacoco")                                          // For code coverage

}