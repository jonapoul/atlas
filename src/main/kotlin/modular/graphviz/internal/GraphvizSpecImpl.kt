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
import modular.graphviz.spec.GraphvizSpec
import modular.graphviz.spec.LayoutEngine
import modular.graphviz.spec.RankDir
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

internal class GraphvizSpecImpl(
  objects: ObjectFactory,
  properties: GradleProperties,
) : GraphvizSpec {
  override val name = NAME
  override val fileExtension = objects.string(convention = "dot")
  override val pathToDotCommand: Property<String> = objects.property(String::class.java).unsetConvention()
  override val adjustSvgViewBox = objects.bool(convention = properties.graphviz.adjustSvgViewBox)
  override val writeReadme = objects.bool(convention = properties.graphviz.writeReadme)

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
  override val arrowHead = objects.string(properties.graphviz.arrowHead)
  override val arrowTail = objects.string(properties.graphviz.arrowTail)
  override val dir = objects.string(properties.graphviz.dir)
  override val dpi = objects.int(properties.graphviz.dpi)
  override val fileFormat = objects.string(properties.graphviz.fileFormat)
  override val fontSize = objects.int(properties.graphviz.fontSize)
  override val layoutEngine = objects.string(properties.graphviz.layoutEngine)
  override val rankDir = objects.string(properties.graphviz.rankDir)
  override val rankSep = objects.float(properties.graphviz.rankSep)

  internal companion object {
    internal const val NAME = "Graphviz"
  }
}
