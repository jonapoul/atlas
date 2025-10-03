/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.internal.IGradleProperties
import modular.core.internal.bool
import modular.core.internal.float
import modular.core.internal.int
import modular.core.internal.string
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class GraphvizGradleProperties(override val project: Project) : IGradleProperties {
  val adjustSvgViewBox: Provider<Boolean> = bool(key = "modular.graphviz.adjustSvgViewBox", default = false)
  val arrowHead: Provider<String> = string(key = "modular.graphviz.chart.arrowHead", default = null)
  val arrowTail: Provider<String> = string(key = "modular.graphviz.chart.arrowTail", default = null)
  val dir: Provider<String> = string(key = "modular.graphviz.chart.dir", default = null)
  val dpi: Provider<Int> = int(key = "modular.graphviz.chart.dpi", default = null)
  val fileFormat: Provider<String> = string(key = "modular.graphviz.chart.fileFormat", default = "svg")
  val fontSize: Provider<Int> = int(key = "modular.graphviz.chart.fontSize", default = null)
  val layoutEngine: Provider<String> = string(key = "modular.graphviz.chart.layoutEngine", default = null)
  val rankDir: Provider<String> = string(key = "modular.graphviz.chart.rankDir", default = null)
  val rankSep: Provider<Float> = float(key = "modular.graphviz.chart.rankSep", default = null)
  val suppressSvgViewBoxWarning: Provider<Boolean> = bool("modular.graphviz.suppressAdjustSvgViewBox", default = false)
}
