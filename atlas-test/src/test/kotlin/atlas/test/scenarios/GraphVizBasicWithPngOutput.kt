package atlas.test.scenarios

import atlas.test.KOTLIN_VERSION
import atlas.test.Scenario

internal object GraphVizBasicWithPngOutput : Scenario by GraphvizBasic {
  override val rootBuildFile = """
    import atlas.graphviz.FileFormat

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      projectTypes {
        kotlinJvm()
        java()
      }

      graphviz {
        fileFormat = FileFormat.Png
      }
    }
  """.trimIndent()
}
