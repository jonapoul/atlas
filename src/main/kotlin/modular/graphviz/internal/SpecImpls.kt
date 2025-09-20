/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.internal.GradleProperties
import modular.core.internal.bool
import modular.core.internal.float
import modular.core.internal.int
import modular.core.internal.set
import modular.core.internal.string
import modular.graphviz.spec.ArrowType
import modular.graphviz.spec.Dir
import modular.graphviz.spec.GraphVizFileFormatSpec
import modular.graphviz.spec.GraphVizSpec
import modular.graphviz.spec.LayoutEngine
import modular.graphviz.spec.RankDir
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable
import java.io.Serializable as JSerializable

internal class GraphVizSpecImpl(
  objects: ObjectFactory,
  properties: GradleProperties,
) : GraphVizSpec {
  override val name = NAME
  override val fileExtension = objects.string(convention = "dot")
  override val pathToDotCommand: Property<String> = objects.property(String::class.java).unsetConvention()
  override val adjustSvgViewBox = objects.bool(convention = properties.adjustSvgViewBox)

  override fun arrowHead(type: ArrowType) = arrowHead(type.string)
  override fun arrowHead(type: String) = arrowHead.set(type)
  override fun arrowTail(type: ArrowType) = arrowTail(type.string)
  override fun arrowTail(type: String) = arrowTail.set(type)
  override fun dir(dir: Dir) = dir(dir.string)
  override fun dir(dir: String) = this.dir.set(dir)
  override fun fileFormats(action: Action<GraphVizFileFormatSpec>) = action.execute(fileFormats)
  override fun layoutEngine(layoutEngine: LayoutEngine) = layoutEngine(layoutEngine.string)
  override fun layoutEngine(layoutEngine: String) = this.layoutEngine.set(layoutEngine)
  override fun rankDir(rankDir: RankDir) = rankDir(rankDir.string)
  override fun rankDir(rankDir: String) = this.rankDir.set(rankDir)
  override val arrowHead = objects.string(properties.arrowHead)
  override val arrowTail = objects.string(properties.arrowTail)
  override val dir = objects.string(properties.dir)
  override val dpi = objects.int(properties.dpi)
  override val fileFormats = GraphVizFileFormatSpecImpl(objects)
  override val fontSize = objects.int(properties.fontSize)
  override val layoutEngine = objects.string(properties.layoutEngine)
  override val rankDir = objects.string(properties.rankDir)
  override val rankSep = objects.float(properties.rankSep)

  internal companion object {
    internal const val NAME = "GraphVizSpecImpl"
  }
}

internal class GraphVizFileFormatSpecImpl(objects: ObjectFactory) : GraphVizFileFormatSpec {
  val formats = objects.set<GraphvizFileFormat>(convention = emptySet())

  override fun add(element: String, includeInReadme: Boolean) =
    formats.add(GraphvizFileFormat(element, includeInReadme))
}

@KSerializable
class GraphvizFileFormat(
  val name: String,
  val includeInReadme: Boolean,
) : JSerializable
