package atlas.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

class ConventionKotlin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginsInternal {
      apply(KotlinPluginWrapper::class)
      apply(ConventionDetekt::class)
      apply(ConventionIdea::class)
      apply(ConventionLicensee::class)
    }

    extensions.configure<KotlinJvmProjectExtension> {
      compilerOptions {
        allWarningsAsErrors.set(true)
        jvmTarget.set(JvmTarget.JVM_21)
        explicitApi()

        freeCompilerArgs.addAll(
          "-opt-in=kotlin.RequiresOptIn",
          "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
          "-opt-in=atlas.core.InternalAtlasApi",
        )
      }
    }

    extensions.configure<JavaPluginExtension> {
      sourceCompatibility = JavaVersion.VERSION_21
      targetCompatibility = JavaVersion.VERSION_21
    }
  }
}
