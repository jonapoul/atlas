import app.cash.licensee.UnusedAction
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
  alias(libs.plugins.binaryCompatibility)
  alias(libs.plugins.dependencyVersions)
  alias(libs.plugins.detekt)
  alias(libs.plugins.dokka)
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.licensee)
  alias(libs.plugins.publish)
  alias(libs.plugins.spotless)
  id("java-gradle-plugin")
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

kotlin {
  compilerOptions {
    allWarningsAsErrors = true
    freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    jvmTarget = JvmTarget.JVM_21
  }
}

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
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
}

tasks.dependencyUpdates {
  rejectVersionIf { !candidate.version.isStable() && currentVersion.isStable() }
}

private fun String.isStable(): Boolean =
  listOf("alpha", "beta", "rc").none { contains(it, ignoreCase = true) }

detekt {
  config.setFrom(file("config/detekt.yml"))
  buildUponDefaultConfig = true
}

val detektTasks = tasks.withType<Detekt>()
val detektCheck by tasks.registering { dependsOn(detektTasks) }
tasks.named("check") { dependsOn(detektCheck) }

detektTasks.configureEach {
  reports.html.required = true
  exclude { it.file.path.contains("generated") }
}

licensee {
  unusedAction(UnusedAction.IGNORE)
  listOf("Apache-2.0", "MIT").forEach(::allow)
}

spotless {
  format("misc") {
    target("*.gradle", "*.md", ".gitignore")
    trimTrailingWhitespace()
    leadingTabsToSpaces(2)
    endWithNewline()
  }

  format("licenseKotlin") {
    licenseHeaderFile("config/spotless.kt", "(package|@file:)")
    target("src/**/*.kt")
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
