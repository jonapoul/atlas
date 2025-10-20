package atlas.test.scenarios

import atlas.test.GraphvizScenario

internal object GroovyGraphVizFull : GraphvizScenario by GroovyBasic {
  override val isGroovy = true

  override val rootBuildFile = """
    import atlas.graphviz.*

    plugins {
      id 'org.jetbrains.kotlin.jvm'
      id 'dev.jonpoulton.atlas.graphviz'
    }

    atlas {
      graphviz {
        fileFormat = FileFormat.Svg
        layoutEngine = LayoutEngine.Circo
      }
    }
  """.trimIndent()
}
