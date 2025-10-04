/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.internal.IGradleProperties
import modular.core.internal.bool
import modular.core.internal.enum
import modular.core.internal.float
import modular.core.internal.int
import modular.graphviz.ArrowType
import modular.graphviz.Dir
import modular.graphviz.FileFormat
import modular.graphviz.LayoutEngine
import modular.graphviz.RankDir
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class GraphvizGradleProperties(override val project: Project) : IGradleProperties {
  val adjustSvgViewBox: Provider<Boolean> = bool("modular.graphviz.adjustSvgViewBox", default = false)
  val arrowHead: Provider<ArrowType> = enum("modular.graphviz.chart.arrowHead", default = null)
  val arrowTail: Provider<ArrowType> = enum("modular.graphviz.chart.arrowTail", default = null)
  val dir: Provider<Dir> = enum("modular.graphviz.chart.dir", default = null)
  val dpi: Provider<Int> = int("modular.graphviz.chart.dpi", default = null)
  val fileFormat: Provider<FileFormat> = enum("modular.graphviz.chart.fileFormat", default = FileFormat.Svg)
  val fontSize: Provider<Int> = int("modular.graphviz.chart.fontSize", default = null)
  val layoutEngine: Provider<LayoutEngine> = enum("modular.graphviz.chart.layoutEngine", default = null)
  val rankDir: Provider<RankDir> = enum("modular.graphviz.chart.rankDir", default = null)
  val rankSep: Provider<Float> = float("modular.graphviz.chart.rankSep", default = null)
  val suppressSvgViewBoxWarning: Provider<Boolean> = bool("modular.graphviz.suppressAdjustSvgViewBox", default = false)
}
