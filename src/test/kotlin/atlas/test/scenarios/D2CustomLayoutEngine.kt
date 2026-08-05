package atlas.test.scenarios

import atlas.test.D2Scenario

internal object D2CustomLayoutEngine : D2Scenario by D2Basic {
  override val atlasConfig =
    """
    projectTypes.useDefaults()

    d2 {
      layoutEngine.elk()
      fileFormat = FileFormat.Svg
    }
    """
      .trimIndent()
}
