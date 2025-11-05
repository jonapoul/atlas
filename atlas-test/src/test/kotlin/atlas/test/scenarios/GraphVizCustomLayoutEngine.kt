package atlas.test.scenarios

import atlas.test.KOTLIN_VERSION
import atlas.test.Scenario
import kotlin.text.trimIndent

internal object GraphVizCustomLayoutEngine : Scenario by GraphVizBigGraph {
  override val rootBuildFile = """
    import atlas.graphviz.FileFormat
    import atlas.graphviz.LayoutEngine.Neato

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      projectTypes {
        kotlinJvm()
      }

      graphviz {
        layoutEngine = Neato
        fileFormat = FileFormat.Svg
      }
    }
  """.trimIndent()
}
