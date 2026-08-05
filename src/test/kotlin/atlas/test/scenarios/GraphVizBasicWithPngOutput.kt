package atlas.test.scenarios

import atlas.test.Scenario

internal object GraphVizBasicWithPngOutput : Scenario by GraphvizBasic {
  override val atlasConfig =
    """
    projectTypes {
      kotlinJvm()
      java()
    }

    graphviz {
      fileFormat = FileFormat.Png
    }
    """
      .trimIndent()
}
