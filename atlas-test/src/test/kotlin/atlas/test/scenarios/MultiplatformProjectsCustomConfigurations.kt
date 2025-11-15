package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.KOTLIN_VERSION

/**
 * Intended to replicate the jvmDev source set added by Compose Hot Reload
 */
internal object MultiplatformProjectsCustomConfigurations : GraphvizScenario {
  override val rootBuildFile = """
    import atlas.graphviz.LinkStyle

    plugins {
      kotlin("multiplatform") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      linkTypes {
        "commonMainImplementation"(LinkStyle.Solid)
        "commonMainApi"(LinkStyle.Dotted)
      }
    }
  """.trimIndent()

  override val subprojectBuildFiles = mapOf(
    "a" to """
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
    """.trimIndent(),

    "b" to """
      plugins { kotlin("multiplatform") }
      kotlin.jvm()
    """.trimIndent(),

    "c" to """
      plugins { kotlin("multiplatform") }
      kotlin.jvm()
    """.trimIndent(),
  )
}
