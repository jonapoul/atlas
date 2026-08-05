package atlas.test.scenarios

import atlas.test.GraphvizScenario

/** Intended to replicate the jvmDev source set added by Compose Hot Reload */
internal object MultiplatformProjectsCustomConfigurations : GraphvizScenario {
  override val rootBuildFile =
    """
    plugins {
      kotlin("multiplatform") apply false
    }
    """
      .trimIndent()

  override val atlasConfig =
    """
    linkTypes {
      "commonMainImplementation"(LinkStyle.Solid)
      "commonMainApi"(LinkStyle.Dotted)
    }
    """
      .trimIndent()

  override val subprojectBuildFiles =
    mapOf(
      "a" to
        """
        plugins { kotlin("multiplatform") }

        kotlin {
          jvm()
          sourceSets {
            commonMain.dependencies {
              implementation(project(":b"))
              api(project(":c"))
            }

            register("jvmDev") {
              dependencies {
                implementation(project(":b"))
              }
            }
          }
        }
        """
          .trimIndent(),
      "b" to
        """
        plugins { kotlin("multiplatform") }
        kotlin.jvm()
        """
          .trimIndent(),
      "c" to
        """
        plugins { kotlin("multiplatform") }
        kotlin.jvm()
        """
          .trimIndent(),
    )
}
