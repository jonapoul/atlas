package modular.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.gradle.util.GradleVersion
import java.io.File
import java.util.Properties

class ConventionTest : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    tasks.withType<Test>().configureEach {
      useJUnitPlatform()

      systemProperty("test.version.gradle", GradleVersion.current().version)
      androidHome()?.let { systemProperty("test.androidHome", it) }

      testLogging {
        events = setOf(PASSED, SKIPPED, FAILED)
        exceptionFormat = FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
        showStandardStreams = true
        displayGranularity = 2
      }
    }

    dependencies {
      "testImplementation"(kotlin("test"))
      "testImplementation"(libs("assertk"))
      "testImplementation"(libs("junit.api"))
      "testRuntimeOnly"(libs("junit.launcher"))
    }
  }

  private fun Project.androidHome(): File? {
    val androidHome = System.getenv("ANDROID_HOME")?.let(::File)
    if (androidHome?.exists() == true) {
      logger.info("Using system environment variable $androidHome as ANDROID_HOME")
      return androidHome
    }

    val localProps = rootProject.file("local.properties")
    if (localProps.exists()) {
      val properties = Properties()
      localProps.inputStream().use { properties.load(it) }
      val sdkHome = properties.getProperty("sdk.dir")?.let(::File)
      if (sdkHome?.exists() == true) {
        logger.info("Using local.properties sdk.dir $sdkHome as ANDROID_HOME")
        return sdkHome
      }
    }

    logger.warn("No Android SDK found - Android unit tests will be skipped")
    return null
  }
}
