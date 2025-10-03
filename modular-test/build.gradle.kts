import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import java.util.Properties

plugins {
  id("modular.convention.plugin")
  alias(libs.plugins.buildConfig)
}

buildConfig {
  generateAtSync = true
  sourceSets.getByName("test") {
    packageName = "modular.test"
    useKotlinOutput { topLevelConstants = true }
    buildConfigField<String>("AGP_VERSION", libs.versions.agp.get())
    buildConfigField<String>("KOTLIN_VERSION", libs.versions.kotlin.get())
  }
}

dependencies {
  testImplementation(kotlin("stdlib"))
  testImplementation(kotlin("test"))
  testImplementation(libs.assertk)
  testImplementation(libs.junit.api)
  testImplementation(project(":modular-core"))
  testImplementation(project(":modular-graphviz"))
  testImplementation(project(":modular-mermaid"))
  testPluginClasspath(project(":modular-graphviz"))
  testPluginClasspath(project(":modular-mermaid"))
  testRuntimeOnly(libs.junit.launcher)
}

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

fun Project.androidHome(): File? {
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
