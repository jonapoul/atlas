/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario
import kotlin.text.trimIndent

object GraphVizChartWithProperties : Scenario by GraphVizBasic {
  override val gradlePropertiesFile = """
    modular.graphviz.chart.arrowHead=halfopen
    modular.graphviz.chart.arrowTail=open
    modular.graphviz.chart.dpi=150
    modular.graphviz.chart.fontSize=20
    modular.graphviz.chart.rankDir=LR
    modular.graphviz.chart.rankSep=2.5
    modular.graphviz.chart.dir=none
  """.trimIndent()
}
