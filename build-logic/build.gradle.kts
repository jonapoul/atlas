import java.util.Properties

plugins {
  `kotlin-dsl`
  idea
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

// Pull java version property from project's root properties file, since build-logic doesn't have access to it
val props = Properties().also { it.load(file("../gradle.properties").reader()) }
val javaInt = props["atlas.javaVersion"]?.toString()?.toInt() ?: error("Failed getting java version from $props")
val javaVersion = JavaVersion.toVersion(javaInt)

java {
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
}

kotlin {
  jvmToolchain(javaInt)
}

dependencies {
  fun compileOnly(plugin: Provider<PluginDependency>) =
    with(plugin.get()) { compileOnly("$pluginId:$pluginId.gradle.plugin:$version") }

  compileOnly(libs.plugins.detekt)
  compileOnly(libs.plugins.dokka)
  compileOnly(libs.plugins.kotlinJvm)
  compileOnly(libs.plugins.licensee)
  compileOnly(libs.plugins.publish)
}

tasks.validatePlugins {
  enableStricterValidation = true
  failOnWarning = true
}

gradlePlugin {
  plugins {
    operator fun String.invoke(impl: String) = register(this) {
      id = this@invoke
      implementationClass = impl
    }

    "atlas.convention.detekt"(impl = "atlas.gradle.ConventionDetekt")
    "atlas.convention.kotlin"(impl = "atlas.gradle.ConventionKotlin")
    "atlas.convention.plugin"(impl = "atlas.gradle.ConventionGradlePlugin")
    "atlas.convention.publish"(impl = "atlas.gradle.ConventionPublish")
  }
}
