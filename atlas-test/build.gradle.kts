import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import java.util.Properties

plugins {
  id("atlas.convention.plugin")
  alias(libs.plugins.buildConfig)
}

tasks.withType<AbstractPublishToMaven>().configureEach { enabled = false }

buildConfig {
  generateAtSync = true
  sourceSets.getByName("test") {
    packageName = "atlas.test"
    useKotlinOutput { topLevelConstants = true }
    buildConfigField("AGP_VERSION", libs.versions.agp)
    buildConfigField("KOTLIN_VERSION", libs.versions.kotlin)
    buildConfigField("GRADLE_VERSION", GradleVersion.current().version)
    buildConfigField<File?>("ANDROID_HOME", androidHome())
  }
}

dependencies {
  testImplementation(kotlin("stdlib"))
  testImplementation(kotlin("test"))
  testImplementation(libs.assertk)
  testImplementation(libs.junit.api)
  testImplementation(libs.junit.params)
  testImplementation(project(":atlas-core"))
  testRuntimeOnly(libs.junit.launcher)

  listOf("d2", "graphviz", "mermaid").forEach {
    testImplementation(project(":atlas-$it"))
    testPluginClasspath(project(":atlas-$it"))
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()

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

fun androidHome(): File? {
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
