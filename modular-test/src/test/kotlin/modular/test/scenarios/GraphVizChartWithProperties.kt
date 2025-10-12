/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario
import kotlin.text.trimIndent

internal object GraphVizChartWithProperties : Scenario by GraphvizBasic {
  override val gradlePropertiesFile = """
    modular.graphviz.fileFormat=gif
    modular.graphviz.layoutEngine=neato
  """.trimIndent()
}
