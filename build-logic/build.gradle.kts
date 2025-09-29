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
  compileOnly(libs.plugins.detekt.plugin())
  compileOnly(libs.plugins.dokka.plugin())
  compileOnly(
    libs.plugins.kotlin.jvm
      .plugin(),
  )
  compileOnly(libs.plugins.licensee.plugin())
  compileOnly(libs.plugins.publish.plugin())
  compileOnly(libs.plugins.spotless.plugin())
}

fun Provider<PluginDependency>.plugin() = get().run { "$pluginId:$pluginId.gradle.plugin:$version" }

tasks.validatePlugins {
  enableStricterValidation = true
  failOnWarning = true
}

gradlePlugin {
  plugins {
    create(id = "modular.convention.detekt", impl = "modular.gradle.ConventionDetekt")
    create(id = "modular.convention.kotlin", impl = "modular.gradle.ConventionKotlin")
    create(id = "modular.convention.plugin", impl = "modular.gradle.ConventionGradlePlugin")
    create(id = "modular.convention.publish", impl = "modular.gradle.ConventionPublish")
    create(id = "modular.convention.spotless", impl = "modular.gradle.ConventionSpotless")
    create(id = "modular.convention.test", impl = "modular.gradle.ConventionTest")
  }
}

fun NamedDomainObjectContainer<PluginDeclaration>.create(
  id: String,
  impl: String,
): PluginDeclaration = create(id) {
  this.id = id
  implementationClass = impl
}
