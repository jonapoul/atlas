/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.InternalModularApi
import modular.core.internal.GradleProperties
import modular.core.internal.bool
import modular.core.internal.float
import modular.core.internal.int
import modular.core.internal.string
import modular.graphviz.ArrowType
import modular.graphviz.Dir
import modular.graphviz.FileFormat
import modular.graphviz.GraphvizSpec
import modular.graphviz.LayoutEngine
import modular.graphviz.RankDir
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

@InternalModularApi
class GraphvizSpecImpl(
  objects: ObjectFactory,
  properties: GradleProperties,
) : GraphvizSpec {
  override val name = "Graphviz"
  override val fileExtension = objects.string(convention = "dot")
  override val pathToDotCommand: Property<String> = objects.property(String::class.java).unsetConvention()
  override val backgroundColor: Property<String> = objects.property(String::class.java).unsetConvention()
  override val adjustSvgViewBox = objects.bool(convention = properties.graphviz.adjustSvgViewBox)

  override val arrowHead = objects.string(properties.graphviz.arrowHead)
  override fun arrowHead(value: ArrowType) = arrowHead(value.string)
  override fun arrowHead(value: String) = arrowHead.set(value)

  override val arrowTail = objects.string(properties.graphviz.arrowTail)
  override fun arrowTail(value: ArrowType) = arrowTail(value.string)
  override fun arrowTail(value: String) = arrowTail.set(value)

  override val dir = objects.string(properties.graphviz.dir)
  override fun dir(value: Dir) = dir(value.string)
  override fun dir(value: String) = dir.set(value)

  override val dpi = objects.int(properties.graphviz.dpi)

  override val fileFormat = objects.string(properties.graphviz.fileFormat)
  override fun fileFormat(value: String) = fileFormat.set(value)
  override fun fileFormat(value: FileFormat) = fileFormat.set(value.string)

  override val fontSize = objects.int(properties.graphviz.fontSize)

  override val layoutEngine = objects.string(properties.graphviz.layoutEngine)
  override fun layoutEngine(value: LayoutEngine) = layoutEngine(value.string)
  override fun layoutEngine(value: String) = layoutEngine.set(value)
  override val rankDir = objects.string(properties.graphviz.rankDir)
  override fun rankDir(value: RankDir) = rankDir(value.string)
  override fun rankDir(value: String) = rankDir.set(value)

  override val rankSep = objects.float(properties.graphviz.rankSep)
}
