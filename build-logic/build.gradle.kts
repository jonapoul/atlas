import blueprint.core.toDependency

plugins {
  `kotlin-dsl`
  idea
  alias(libs.plugins.blueprint) apply false
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

// Read the java version from the project's root file, since build-logic's own root is this
// directory
val javaInt =
  providers
    .fileContents(layout.projectDirectory.file("../.java-version"))
    .asText
    .map { it.trim().toInt() }
    .get()
val javaVersion = JavaVersion.toVersion(javaInt)

java {
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
}

kotlin {
  jvmToolchain(javaInt)
}

dependencies {
  fun compileOnlyPlugin(plugin: Provider<PluginDependency>) = compileOnly(plugin.toDependency())

  compileOnlyPlugin(libs.plugins.detekt)
  compileOnlyPlugin(libs.plugins.dokka)
  compileOnlyPlugin(libs.plugins.kotlinJvm)
  compileOnlyPlugin(libs.plugins.licensee)
  compileOnlyPlugin(libs.plugins.publish)

  implementation(libs.blueprint.core)
}

tasks.validatePlugins {
  enableStricterValidation = true
  failOnWarning = true
}

gradlePlugin {
  plugins {
    operator fun String.invoke(impl: String) =
      register(this) {
        id = this@invoke
        implementationClass = impl
      }

    "atlas.convention.detekt"(impl = "atlas.gradle.ConventionDetekt")
    "atlas.convention.kotlin"(impl = "atlas.gradle.ConventionKotlin")
    "atlas.convention.plugin"(impl = "atlas.gradle.ConventionGradlePlugin")
    "atlas.convention.publish"(impl = "atlas.gradle.ConventionPublish")
  }
}
