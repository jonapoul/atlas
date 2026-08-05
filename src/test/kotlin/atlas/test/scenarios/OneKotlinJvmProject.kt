package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.kotlinJvmBuildScript

internal object OneKotlinJvmProject : GraphvizScenario {
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

  override val subprojectBuildFiles = mapOf("test-jvm" to kotlinJvmBuildScript)
}
