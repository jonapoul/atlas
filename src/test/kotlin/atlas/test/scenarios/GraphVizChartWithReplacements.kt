package atlas.test.scenarios

import atlas.test.Scenario

internal object GraphVizChartWithReplacements : Scenario by GraphvizBasic {
  override val atlasConfig =
    """
    projectTypes {
      kotlinJvm()
      java()
    }

    pathTransforms {
      replace(pattern = "^:", replacement = "") // remove ":" prefix
      replace(pattern = "^b$", replacement = "B") // rename one project to uppercase
    }
    """
      .trimIndent()
}
