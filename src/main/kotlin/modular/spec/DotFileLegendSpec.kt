/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.internal.ModularProperties
import modular.internal.int
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

class DotFileLegendSpec(objects: ObjectFactory, project: Project) {
  private val properties = ModularProperties(project)

  val cellBorder: Property<Int> = objects.int(properties.cellBorder)
  val cellPadding: Property<Int> = objects.int(properties.cellPadding)
  val cellSpacing: Property<Int> = objects.int(properties.cellSpacing)
  val tableBorder: Property<Int> = objects.int(properties.tableBorder)
}
