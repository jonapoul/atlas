/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object GraphVizChartWithProperties : Scenario by GraphVizBasic {
  override val gradlePropertiesFile = """
    modular.graphViz.chart.arrowHead=halfopen
    modular.graphViz.chart.arrowTail=open
    modular.graphViz.chart.dpi=150
    modular.graphViz.chart.fontSize=20
    modular.graphViz.chart.rankDir=LR
    modular.graphViz.chart.rankSep=2.5
    modular.graphViz.chart.dir=none
  """.trimIndent()
}
