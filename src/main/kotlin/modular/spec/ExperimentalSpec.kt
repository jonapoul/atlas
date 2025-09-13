/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import javax.inject.Inject

/**
 * Used in [modular.gradle.ModularExtension]
 */
interface ExperimentalSpec {
  val adjustSvgViewBox: Property<Boolean>
}

/**
 * Injected in [modular.tasks.GenerateGraphvizFileTask]
 */
@Suppress("UnnecessaryAbstractClass")
abstract class ExperimentalFlags @Inject constructor() {
  @get:Input abstract val adjustSvgViewBox: Property<Boolean>
}
