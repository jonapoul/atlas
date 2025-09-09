/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.internal.ModularProperties
import modular.internal.bool
import modular.internal.enum
import modular.internal.float
import modular.internal.int
import modular.internal.string
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

class DotFileChartSpec(objects: ObjectFactory, project: Project) : ChartSpec {
  private val properties = ModularProperties(project)

  override val file: RegularFileProperty = objects
    .fileProperty()
    .convention(
      project.rootProject
        .layout
        .projectDirectory
        .file("modules.dot"),
    )

  val arrowHead: Property<String> = objects.string(properties.arrowHead)
  val arrowTail: Property<String> = objects.string(properties.arrowTail)
  val dpi: Property<Int> = objects.int(properties.dpi)
  val fontSize: Property<Int> = objects.int(properties.fontSize)
  val rankDir: Property<RankDir> = objects.enum(properties.rankDir)
  val rankSep: Property<Float> = objects.float(properties.rankSep)
  val showArrows: Property<Boolean> = objects.bool(properties.showArrows)

  fun arrowHead(type: ArrowType) = arrowHead.set(type.toString())
  fun arrowTail(type: ArrowType) = arrowTail.set(type.toString())
}
