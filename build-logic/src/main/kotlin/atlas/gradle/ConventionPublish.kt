package atlas.gradle

import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationExtension
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

class ConventionPublish : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginsInternal {
      apply(MavenPublishPlugin::class)
      apply(ConventionDokka::class)
    }

    extensions.configure<MavenPublishBaseExtension> {
      publishToMavenCentral(automaticRelease = true)
      signAllPublications()
      configure(KotlinJvm(JavadocJar.Dokka("dokkaGeneratePublicationHtml"), sourcesJar = true))
    }

    extensions.configure<KotlinJvmProjectExtension> {
      // https://kotlinlang.org/docs/gradle-binary-compatibility-validation.html
      @OptIn(ExperimentalAbiValidation::class)
      extensions.configure<AbiValidationExtension> {
        enabled.set(true)
        filters {
          excluded {
            annotatedWith.add("atlas.core.InternalAtlasApi")
          }
        }
      }
    }
  }
}
