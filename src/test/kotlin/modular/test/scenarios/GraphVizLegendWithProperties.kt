/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object GraphVizLegendWithProperties : Scenario by GraphVizBasicWithBasicLegend {
  override val gradlePropertiesFile = """
    modular.dotfile.legend.tableBorder=10
    modular.dotfile.legend.cellBorder=20
    modular.dotfile.legend.cellSpacing=30
    modular.dotfile.legend.cellPadding=40
  """.trimIndent()
}
