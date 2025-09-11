/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.internal.ModularProperties
import modular.internal.bool
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import javax.inject.Inject

/**
 * Used in [modular.gradle.ModularExtension]
 */
class ExperimentalSpec(objects: ObjectFactory, project: Project) {
  private val properties = ModularProperties(project)

  val adjustSvgViewBox: Property<Boolean> = objects.bool(convention = properties.adjustSvgViewBox)
}

/**
 * Injected in [modular.tasks.GenerateGraphvizFileTask]
 */
@Suppress("UnnecessaryAbstractClass")
abstract class ExperimentalFlags @Inject constructor() {
  @get:Input abstract val adjustSvgViewBox: Property<Boolean>
}
