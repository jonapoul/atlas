package atlas.gradle

import blueprint.core.javaVersion
import blueprint.core.jvmTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

class ConventionKotlin : Plugin<Project> {
  override fun apply(target: Project) =
    with(target) {
      pluginsInternal {
        apply(KotlinPluginWrapper::class)
        apply(ConventionDetekt::class)
        apply(ConventionIdea::class)
        apply(ConventionLicensee::class)
      }

      extensions.configure<KotlinJvmProjectExtension> {
        compilerOptions {
          allWarningsAsErrors.set(true)
          this.jvmTarget.set(jvmTarget())
          explicitApi()

          freeCompilerArgs.addAll(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
            "-opt-in=atlas.core.InternalAtlasApi",
          )
        }
      }

      extensions.configure<JavaPluginExtension> {
        val version = javaVersion().get()
        sourceCompatibility = version
        targetCompatibility = version
      }
    }
}
