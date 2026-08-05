package atlas.test.scenarios

import atlas.test.D2Scenario
import atlas.test.kotlinJvmBuildScript

internal object D2NestedProjects : D2Scenario {
  override val rootBuildFile =
    """
    plugins {
      kotlin("jvm") apply false
    }
    """
      .trimIndent()

  override val subprojectBuildFiles = mapOf("path:to:my:project" to kotlinJvmBuildScript)
}
