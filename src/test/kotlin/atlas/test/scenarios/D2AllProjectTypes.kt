package atlas.test.scenarios

import atlas.test.D2Scenario

internal object D2AllProjectTypes : D2Scenario by D2Basic {
  override val atlasConfig =
    """
    projectTypes {
      androidApp()
      kotlinMultiplatform()
      androidLibrary()
      kotlinJvm()
      java()
      other()
    }
    """
      .trimIndent()
}
