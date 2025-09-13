/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object DotFileChartWithProperties : Scenario by DotFileBasic {
  override val gradlePropertiesFile = """
    modular.dotfile.chart.arrowHead=halfopen
    modular.dotfile.chart.arrowTail=open
    modular.dotfile.chart.dpi=150
    modular.dotfile.chart.fontSize=20
    modular.dotfile.chart.rankDir=LR
    modular.dotfile.chart.rankSep=2.5
    modular.dotfile.chart.dir=none
  """.trimIndent()
}
