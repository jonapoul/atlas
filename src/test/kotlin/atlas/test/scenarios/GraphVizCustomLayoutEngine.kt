package atlas.test.scenarios

import atlas.test.Scenario

internal object GraphVizCustomLayoutEngine : Scenario by GraphVizBigGraph {
  override val atlasConfig =
    """
    projectTypes {
      kotlinJvm()
    }

    graphviz {
      layoutEngine = LayoutEngine.Neato
      fileFormat = FileFormat.Svg
    }
    """
      .trimIndent()
}
