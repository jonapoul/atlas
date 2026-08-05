package atlas.test.scenarios

import atlas.test.Scenario

internal object GraphVizChartCustomConfig : Scenario by GraphvizBasic {
  override val atlasConfig =
    """
    projectTypes {
      kotlinJvm()
      java()
      hasPluginId(name = "Custom", color = "#123456", pluginId = "com.something.whatever")
    }

    graphviz {
      fileFormat = FileFormat.Gif
      layoutEngine = LayoutEngine.TwoPi

      edge {
        arrowHead = ArrowType.HalfOpen
        arrowTail = ArrowType.Open
      }

      graph {
        dpi = 150
      }

      node {
        shape = Shape.None
      }
    }
    """
      .trimIndent()
}
