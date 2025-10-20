package atlas.test.scenarios

import atlas.test.Scenario
import kotlin.text.trimIndent

internal object GraphVizChartWithProperties : Scenario by GraphvizBasic {
  override val gradlePropertiesFile = """
    atlas.graphviz.fileFormat=gif
    atlas.graphviz.layoutEngine=neato
  """.trimIndent()
}
