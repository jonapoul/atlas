/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.internal.IGradleProperties
import modular.core.internal.bool
import modular.core.internal.float
import modular.core.internal.int
import modular.core.internal.mapEnum
import modular.core.internal.string
import modular.graphviz.ArrowType
import modular.graphviz.Dir
import modular.graphviz.FileFormat
import modular.graphviz.LayoutEngine
import modular.graphviz.RankDir
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class GraphvizGradleProperties(override val project: Project) : IGradleProperties {
  val adjustSvgViewBox: Provider<Boolean> = bool("modular.graphviz.adjustSvgViewBox", default = false)
  val arrowHead: Provider<ArrowType> = string("modular.graphviz.chart.arrowHead", default = null).mapEnum()
  val arrowTail: Provider<ArrowType> = string("modular.graphviz.chart.arrowTail", default = null).mapEnum()
  val dir: Provider<Dir> = string("modular.graphviz.chart.dir", default = null).mapEnum()
  val dpi: Provider<Int> = int("modular.graphviz.chart.dpi", default = null)
  val fileFormat: Provider<FileFormat> = string("modular.graphviz.chart.fileFormat", default = "svg").mapEnum()
  val fontSize: Provider<Int> = int("modular.graphviz.chart.fontSize", default = null)
  val layoutEngine: Provider<LayoutEngine> = string("modular.graphviz.chart.layoutEngine", default = null).mapEnum()
  val rankDir: Provider<RankDir> = string("modular.graphviz.chart.rankDir", default = null).mapEnum()
  val rankSep: Provider<Float> = float("modular.graphviz.chart.rankSep", default = null)
  val suppressSvgViewBoxWarning: Provider<Boolean> = bool("modular.graphviz.suppressAdjustSvgViewBox", default = false)
}
