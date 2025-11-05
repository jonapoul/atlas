package atlas.test.scenarios

import atlas.test.KOTLIN_VERSION
import atlas.test.Scenario
import kotlin.text.trimIndent

internal object GraphVizChartWithReplacements : Scenario by GraphvizBasic {
  override val rootBuildFile = """
    import atlas.graphviz.ArrowType
    import atlas.graphviz.RankDir

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      projectTypes {
        kotlinJvm()
        java()
      }

      pathTransforms {
        replace(pattern = "^:", replacement = "") // remove ":" prefix
        replace(pattern = "^b$", replacement = "B") // rename one project to uppercase
      }
    }
  """.trimIndent()
}
