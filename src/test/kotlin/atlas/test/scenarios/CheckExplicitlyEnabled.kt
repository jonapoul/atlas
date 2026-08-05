package atlas.test.scenarios

import atlas.test.Scenario

internal object CheckExplicitlyEnabled : Scenario by GraphvizBasic {
  override val atlasConfig =
    """
    projectTypes {
      kotlinJvm()
      java()
    }

    checkOutputs = true
    """
      .trimIndent()
}
