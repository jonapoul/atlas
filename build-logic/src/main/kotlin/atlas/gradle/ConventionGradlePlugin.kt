package atlas.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.plugin.GradlePluginApiVersion
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
  private val Project.skipPublish
    get() = providers
      .gradleProperty("atlas.skipPublish")
      .map { it.toBoolean() }
      .getOrElse(false)

  override fun apply(target: Project): Unit = with(target) {
    pluginsInternal {
      apply(ConventionKotlin::class)
      apply(ConventionLicensee::class)
      if (!skipPublish) apply(ConventionPublish::class)
      apply(JavaGradlePluginPlugin::class)
      apply("org.jetbrains.kotlin.plugin.serialization")
    }

    extensions.configure<GradlePluginDevelopmentExtension> {
      vcsUrl.set("https://github.com/jonapoul/atlas-gradle-plugin.git")
      website.set("https://github.com/jonapoul/atlas-gradle-plugin")

      plugins.configureEach {
        description = providers.gradleProperty("POM_DESCRIPTION").get()
        tags.addAll("gradle", "kotlin", "modules", "projects", "diagrams", "charts", "links")
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

    // Set the minimum supported gradle version
    val minimumGradleVersion = providers.gradleProperty("atlas.minimumGradleVersion")
    configurations.named("apiElements").configure {
      attributes {
        attribute(
          GradlePluginApiVersion.GRADLE_PLUGIN_API_VERSION_ATTRIBUTE,
          objects.named(GradlePluginApiVersion::class.java, minimumGradleVersion.get()),
        )
      }
    }
  }
}
