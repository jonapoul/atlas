package atlas.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier.Public

class ConventionDokka : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginsInternal {
      apply(DokkaPlugin::class)
    }

    extensions.configure<DokkaExtension> {
      moduleName.set(path)

      dokkaPublications.configureEach {
        failOnWarning.set(true)
        suppressInheritedMembers.set(true)
        suppressObviousFunctions.set(true)
      }

      dokkaSourceSets.configureEach {
        documentedVisibilities.add(Public)
        reportUndocumented.set(false)
        skipDeprecated.set(true)
        suppressGeneratedFiles.set(true)

        perPackageOption {
          matchingRegex.set(".*\\.internal.*")
          suppress.set(true)
        }

        sourceLink {
          localDirectory.set(layout.projectDirectory)
          remoteLineSuffix.set("#L")
          val path = project.path.replace(":", "")
          remoteUrl("https://github.com/jonapoul/atlas-gradle-plugin/tree/main/$path")
        }
      }
    }
  }
}
