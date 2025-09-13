/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object GraphVizLegendWithProperties : Scenario by GraphVizBasicWithBasicLegend {
  override val gradlePropertiesFile = """
    modular.graphViz.legend.tableBorder=10
    modular.graphViz.legend.cellBorder=20
    modular.graphViz.legend.cellSpacing=30
    modular.graphViz.legend.cellPadding=40
  """.trimIndent()
}
