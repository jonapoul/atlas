package modular.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.plugin.devel.plugins.JavaGradlePluginPlugin
import org.gradle.plugin.devel.tasks.PluginUnderTestMetadata
import org.gradle.plugin.devel.tasks.ValidatePlugins

class ConventionGradlePlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    pluginsInternal {
      apply(ConventionKotlin::class)
      apply(ConventionSpotless::class)
      apply(ConventionLicensee::class)
      apply(ConventionPublish::class)
      apply(JavaGradlePluginPlugin::class)
      apply("org.jetbrains.kotlin.plugin.serialization")
    }

    extensions.configure<GradlePluginDevelopmentExtension> {
      vcsUrl.set("https://github.com/jonapoul/modular.git")
      website.set("https://github.com/jonapoul/modular")

      plugins.configureEach {
        description = properties["POM_DESCRIPTION"] as String
        tags.addAll("gradle", "kotlin", "modules", "diagrams", "charts", "links")
      }
    }

    val testPluginClasspath by configurations.registering { isCanBeResolved = true }

    dependencies {
      "compileOnly"(libs("kotlin.gradle"))
      "implementation"(libs("kotlinx.serialization"))
      testPluginClasspath(libs("agp"))
      testPluginClasspath(libs("kotlin.gradle"))
    }

    tasks.withType<ValidatePlugins>().configureEach {
      enableStricterValidation.set(true)
      failOnWarning.set(true)
    }

    // Plugins used in tests could be resolved in classpath
    tasks.withType<PluginUnderTestMetadata> {
      pluginClasspath.from(testPluginClasspath)
    }
  }
}
