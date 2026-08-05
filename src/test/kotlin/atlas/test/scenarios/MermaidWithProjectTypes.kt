package atlas.test.scenarios

import atlas.test.Scenario

internal object MermaidWithProjectTypes : Scenario by MermaidBasic {
  override val atlasConfig =
    """
    projectTypes {
      kotlinJvm()
      java()
    }
    """
      .trimIndent()
}
