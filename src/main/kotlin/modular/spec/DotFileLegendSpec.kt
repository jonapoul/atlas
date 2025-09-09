/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.internal.ModularProperties
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

class DotFileLegendSpec(objects: ObjectFactory, project: Project) : LegendSpec {
  private val properties = ModularProperties(project)

  override val file: RegularFileProperty = objects
    .fileProperty()
    .convention(
      project.rootProject
        .layout
        .projectDirectory
        .file("modules-legend.dot"),
    )

  val tableBorder: Property<Int> = objects.property(Int::class.java).convention(properties.tableBorder)
  val cellBorder: Property<Int> = objects.property(Int::class.java).convention(properties.cellBorder)
  val cellSpacing: Property<Int> = objects.property(Int::class.java).convention(properties.cellSpacing)
  val cellPadding: Property<Int> = objects.property(Int::class.java).convention(properties.cellPadding)
}
