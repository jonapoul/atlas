import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import java.util.Properties

plugins {
  id("modular.convention.kotlin")
  id("modular.convention.plugin")
  id("modular.convention.style")
}

gradlePlugin {
  vcsUrl = "https://github.com/jonapoul/modular"
  website = "https://github.com/jonapoul/modular"

  plugins {
    val allTags = listOf("gradle", "kotlin", "modules", "diagrams", "markdown", "dotfile", "chart")
    val desc = properties["POM_DESCRIPTION"] as String

    create("trunk") {
      id = "dev.jonpoulton.modular.trunk"
      implementationClass = "modular.gradle.ModularTrunkPlugin"
      displayName = "Modular Trunk"
      description = desc
      tags.addAll(allTags)
    }

    create("leaf") {
      id = "dev.jonpoulton.modular.leaf"
      implementationClass = "modular.gradle.ModularLeafPlugin"
      displayName = "Modular Leaf"
      description = desc
      tags.addAll(allTags)
    }
  }
}

val javaInt = properties["modular.javaVersion"]?.toString()?.toInt() ?: error("Failed getting java version")
val javaVersion = JavaVersion.toVersion(javaInt)

kotlin {
  jvmToolchain(javaInt)
  compilerOptions {
    allWarningsAsErrors = true
    freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
  }
}

java {
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
}

val testPluginClasspath by configurations.registering { isCanBeResolved = true }

dependencies {
  compileOnly(libs.kotlin.gradle)

  testImplementation(gradleTestKit())
  testImplementation(kotlin("test"))
  testImplementation(libs.assertk)
  testImplementation(libs.junit.api)
  testRuntimeOnly(libs.junit.launcher)

  testPluginClasspath(libs.agp)
  testPluginClasspath(libs.kotlin.gradle)
}

tasks.validatePlugins {
  enableStricterValidation = true
  failOnWarning = true
}

// Plugins used in tests could be resolved in classpath
tasks.pluginUnderTestMetadata {
  pluginClasspath.from(testPluginClasspath)
}

tasks.test {
  useJUnitPlatform()

  systemProperty("test.version.gradle", GradleVersion.current().version)
  androidHome()?.let { systemProperty("test.androidHome", it) }

  testLogging {
    events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
    exceptionFormat = TestExceptionFormat.FULL
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
