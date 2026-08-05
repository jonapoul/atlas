package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.kotlinJvmBuildScript

internal object DiamondGraph : GraphvizScenario {
  override val rootBuildFile =
    """
    plugins {
      kotlin("jvm") apply false
    }
    """
      .trimIndent()

  override val atlasConfig =
    """
    projectTypes {
      kotlinJvm()
    }
    """
      .trimIndent()

  override val subprojectBuildFiles =
    mapOf(
      "top" to
        """
      $kotlinJvmBuildScript
      dependencies {
        api(project(":mid-a"))
        implementation(project(":mid-b"))
      }
    """
          .trimIndent(),
      "mid-a" to
        """
      $kotlinJvmBuildScript
      dependencies {
        api(project(":bottom"))
      }
    """
          .trimIndent(),
      "mid-b" to
        """
      $kotlinJvmBuildScript
      dependencies {
        implementation(project(":bottom"))
      }
    """
          .trimIndent(),
      "bottom" to kotlinJvmBuildScript,
    )
}
