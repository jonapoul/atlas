package atlas.test.scenarios

import atlas.test.KOTLIN_VERSION
import atlas.test.Scenario
import kotlin.text.trimIndent

internal object GraphVizChartCustomConfig : Scenario by GraphvizBasic {
  override val rootBuildFile = """
    import atlas.graphviz.ArrowType
    import atlas.graphviz.Dir
    import atlas.graphviz.FileFormat
    import atlas.graphviz.LayoutEngine
    import atlas.graphviz.RankDir
    import atlas.graphviz.Shape

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      moduleTypes {
        kotlinJvm()
        java()
        registerByPluginId(name = "Custom", color = "#123456", pluginId = "com.something.whatever")
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
    }
  """.trimIndent()
}
