package atlas.test.scenarios

import atlas.test.KOTLIN_VERSION
import atlas.test.Scenario
import kotlin.text.trimIndent

internal object GraphVizCustomDotExecutable : Scenario by GraphvizBasic {
  override val rootBuildFile = """
    import atlas.graphviz.FileFormat
    import atlas.graphviz.LayoutEngine.Neato

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      moduleTypes {
        kotlinJvm()
        java()
      }

      graphviz {
        pathToDotCommand = file("path/to/custom/dot").absolutePath
        fileFormat = FileFormat.Svg
      }
    }
  """.trimIndent()
}
