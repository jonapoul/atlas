/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.internal.gradleIntProperty
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

class DotFileLegendSpec(objects: ObjectFactory, project: Project) : LegendSpec {
  override val file: RegularFileProperty = objects
    .fileProperty()
    .convention(
      project.rootProject
        .layout
        .projectDirectory
        .file("modules-legend.dot"),
    )

  val tableBorder: Property<Int> = objects
    .property<Int>()
    .convention(project.gradleIntProperty(key = "modular.dotfile.legend.tableBorder", default = 0))

  val cellBorder: Property<Int> = objects
    .property<Int>()
    .convention(project.gradleIntProperty(key = "modular.dotfile.legend.cellBorder", default = 1))

  val cellSpacing: Property<Int> = objects
    .property<Int>()
    .convention(project.gradleIntProperty(key = "modular.dotfile.legend.cellSpacing", default = 0))

  val cellPadding: Property<Int> = objects
    .property<Int>()
    .convention(project.gradleIntProperty(key = "modular.dotfile.legend.cellPadding", default = 4))
}

