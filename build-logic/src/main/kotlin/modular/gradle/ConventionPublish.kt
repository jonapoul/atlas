package modular.gradle

import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationExtension
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

class ConventionPublish : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginsInternal {
      apply(MavenPublishPlugin::class)
      apply(DokkaPlugin::class)
    }

    extensions.configure<KotlinJvmProjectExtension> {
      // https://kotlinlang.org/docs/gradle-binary-compatibility-validation.html
      @OptIn(ExperimentalAbiValidation::class)
      extensions.configure<AbiValidationExtension> {
        enabled.set(true)
        filters {
          excluded {
            annotatedWith.add("modular.core.InternalModularApi")
          }
        }
      }
    }
  }
}
