package atlas.test.scenarios

import atlas.test.Scenario

internal object CheckExplicitlyDisabled : Scenario by GraphvizBasic {
  override val atlasConfig = "checkOutputs = false"
}
