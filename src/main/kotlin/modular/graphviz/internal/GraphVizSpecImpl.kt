/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.internal.GradleProperties
import modular.core.internal.bool
import modular.core.internal.float
import modular.core.internal.int
import modular.core.internal.string
import modular.graphviz.spec.ArrowType
import modular.graphviz.spec.Dir
import modular.graphviz.spec.FileFormat
import modular.graphviz.spec.GraphVizSpec
import modular.graphviz.spec.LayoutEngine
import modular.graphviz.spec.RankDir
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

internal class GraphVizSpecImpl(
  objects: ObjectFactory,
  properties: GradleProperties,
) : GraphVizSpec {
  override val name = NAME
  override val fileExtension = objects.string(convention = "dot")
  override val pathToDotCommand: Property<String> = objects.property(String::class.java).unsetConvention()
  override val adjustSvgViewBox = objects.bool(convention = properties.adjustSvgViewBox)
  override val writeReadme = objects.bool(convention = properties.gvWriteReadme)

  override fun arrowHead(value: ArrowType) = arrowHead(value.string)
  override fun arrowHead(value: String) = arrowHead.set(value)
  override fun arrowTail(value: ArrowType) = arrowTail(value.string)
  override fun arrowTail(value: String) = arrowTail.set(value)
  override fun dir(value: Dir) = dir(value.string)
  override fun dir(value: String) = dir.set(value)
  override fun fileFormat(value: String) = fileFormat.set(value)
  override fun fileFormat(value: FileFormat) = fileFormat.set(value.string)
  override fun layoutEngine(value: LayoutEngine) = layoutEngine(value.string)
  override fun layoutEngine(value: String) = layoutEngine.set(value)
  override fun rankDir(value: RankDir) = rankDir(value.string)
  override fun rankDir(value: String) = rankDir.set(value)
  override val arrowHead = objects.string(properties.arrowHead)
  override val arrowTail = objects.string(properties.arrowTail)
  override val dir = objects.string(properties.dir)
  override val dpi = objects.int(properties.dpi)
  override val fileFormat = objects.string(properties.fileFormat)
  override val fontSize = objects.int(properties.fontSize)
  override val layoutEngine = objects.string(properties.layoutEngine)
  override val rankDir = objects.string(properties.rankDir)
  override val rankSep = objects.float(properties.rankSep)

  internal companion object {
    internal const val NAME = "Graphviz"
  }
}
