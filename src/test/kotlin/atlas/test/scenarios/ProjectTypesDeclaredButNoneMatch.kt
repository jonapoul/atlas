package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.kotlinJvmBuildScript

internal object ProjectTypesDeclaredButNoneMatch : GraphvizScenario {
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
      androidApp()
      kotlinMultiplatform()
      androidLibrary()
    }
    """
      .trimIndent()

  override val subprojectBuildFiles = mapOf("test-jvm" to kotlinJvmBuildScript)
}
