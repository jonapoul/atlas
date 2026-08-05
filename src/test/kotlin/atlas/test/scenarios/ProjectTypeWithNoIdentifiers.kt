package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.kotlinJvmBuildScript

internal object ProjectTypeWithNoIdentifiers : GraphvizScenario {
  override val rootBuildFile =
    """
    plugins {
      kotlin("jvm") apply false
    }
    """
      .trimIndent()

  override val atlasConfig = """projectTypes { create("custom") }"""

  override val subprojectBuildFiles = mapOf("test-jvm" to kotlinJvmBuildScript)
}
