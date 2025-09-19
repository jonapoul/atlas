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
val javaInt = props["modular.javaVersion"]?.toString()?.toInt() ?: error("Failed getting java version from $props")
val javaVersion = JavaVersion.toVersion(javaInt)

java {
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
}

kotlin {
  jvmToolchain(javaInt)
}

dependencies {
  compileOnly(libs.kotlin.gradle)

  compileOnly(libs.plugins.agp.app.toDependency())
  compileOnly(libs.plugins.agp.lib.toDependency())
  compileOnly(libs.plugins.binaryCompatibility.toDependency())
  compileOnly(libs.plugins.detekt.toDependency())
  compileOnly(libs.plugins.dokka.toDependency())
  compileOnly(libs.plugins.licensee.toDependency())
  compileOnly(libs.plugins.publish.toDependency())
  compileOnly(libs.plugins.spotless.toDependency())
}

tasks.validatePlugins {
  enableStricterValidation = true
  failOnWarning = true
}

gradlePlugin {
  plugins {
    create(id = "modular.convention.kotlin", impl = "modular.gradle.ConventionKotlin")
    create(id = "modular.convention.plugin", impl = "modular.gradle.ConventionPlugin")
    create(id = "modular.convention.style", impl = "modular.gradle.ConventionStyle")
  }
}

fun NamedDomainObjectContainer<PluginDeclaration>.create(
  id: String,
  impl: String,
): PluginDeclaration = create(id) {
  this.id = id
  implementationClass = impl
}


fun Provider<PluginDependency>.toDependency(): String = with(get()) {
  "$pluginId:$pluginId.gradle.plugin:$version"
}
