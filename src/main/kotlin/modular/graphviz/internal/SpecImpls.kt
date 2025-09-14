/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.graphviz.spec.ArrowType
import modular.graphviz.spec.Dir
import modular.graphviz.spec.GraphVizChartSpec
import modular.graphviz.spec.GraphVizFileFormatSpec
import modular.graphviz.spec.GraphVizLegendSpec
import modular.graphviz.spec.GraphVizSpec
import modular.graphviz.spec.LayoutEngine
import modular.graphviz.spec.RankDir
import modular.internal.GradleProperties
import modular.internal.float
import modular.internal.int
import modular.internal.string
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

internal class GraphVizSpecImpl(
  private val objects: ObjectFactory,
  private val properties: GradleProperties,
) : GraphVizSpec {
  override val name = NAME
  override val fileExtension = objects.string(convention = "dot")
  override val pathToDotCommand: Property<String> = objects.property(String::class.java).unsetConvention()

  override var legend: GraphVizLegendSpec? = null
  override fun legend() = legend { /* no-op */ }
  override fun legend(action: Action<GraphVizLegendSpec>) = action.execute(getOrBuildLegend())

  override val chart = GraphVizChartSpecImpl(objects, properties)

  override val fileFormats = GraphVizFileFormatSpecImpl(objects)
  override fun fileFormats(action: Action<GraphVizFileFormatSpec>) = action.execute(fileFormats)

  private fun getOrBuildLegend() = legend ?: GraphVizLegendSpecImpl(objects, properties).also { legend = it }

  internal companion object {
    internal const val NAME = "GraphVizSpecImpl"
  }
}

internal class GraphVizLegendSpecImpl(objects: ObjectFactory, properties: GradleProperties) : GraphVizLegendSpec {
  override val cellBorder = objects.int(properties.cellBorder)
  override val cellPadding = objects.int(properties.cellPadding)
  override val cellSpacing = objects.int(properties.cellSpacing)
  override val tableBorder = objects.int(properties.tableBorder)
}

internal class GraphVizChartSpecImpl(objects: ObjectFactory, properties: GradleProperties) : GraphVizChartSpec {
  override fun arrowHead(type: ArrowType) = arrowHead(type.string)
  override fun arrowHead(type: String) = arrowHead.set(type)
  override fun arrowTail(type: ArrowType) = arrowTail(type.string)
  override fun arrowTail(type: String) = arrowTail.set(type)
  override fun dir(dir: Dir) = dir(dir.string)
  override fun dir(dir: String) = this.dir.set(dir)
  override fun layoutEngine(layoutEngine: LayoutEngine) = layoutEngine(layoutEngine.string)
  override fun layoutEngine(layoutEngine: String) = this.layoutEngine.set(layoutEngine)
  override fun rankDir(rankDir: RankDir) = rankDir(rankDir.string)
  override fun rankDir(rankDir: String) = this.rankDir.set(rankDir)
  override val arrowHead = objects.string(properties.arrowHead)
  override val arrowTail = objects.string(properties.arrowTail)
  override val dir = objects.string(properties.dir)
  override val dpi = objects.int(properties.dpi)
  override val fontSize = objects.int(properties.fontSize)
  override val layoutEngine = objects.string(properties.layoutEngine)
  override val rankDir = objects.string(properties.rankDir)
  override val rankSep = objects.float(properties.rankSep)
}

internal class GraphVizFileFormatSpecImpl(objects: ObjectFactory) :
  GraphVizFileFormatSpec,
  SetProperty<String> by objects.setProperty(String::class.java)
