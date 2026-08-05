package atlas.test.scenarios

import atlas.test.GraphvizScenario

internal object NoSubprojects : GraphvizScenario {
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
}
