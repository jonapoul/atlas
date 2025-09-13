/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.gradle.ExperimentalModularApi
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import javax.inject.Inject

/**
 * Used in [modular.gradle.ModularExtension]
 */
interface ExperimentalSpec {
  /**
   * When enabled, the generated GraphViz SVG file will have its `viewBox` parameter auto-adjusted to make sure all the
   * contents fit in the visible frame. This is to work around an issue I've seen where the custom DPI value tends to
   * make the bottom right of the chart clipped out of the view box.
   *
   * Not necessary unless you have:
   * a) configured GraphViz as an output
   * b) have specified SVG output file for GraphViz
   * c) have configured a custom DPI value for GraphViz
   *
   * You can either enable this flag in the Gradle script, or set the following in your `gradle.properties` file:
   *
   * ```properties
   * modular.experimental.adjustSvgViewBox=true
   * ```
   *
   * Disabled by default.
   *
   * If you don't want this adjustment to happen at all, you can suppress the Gradle warning with the following in
   * `gradle.properties`:
   *
   * ```properties
   * modular.suppress.adjustSvgViewBox=true
   * ```
   */
  @ExperimentalModularApi
  val adjustSvgViewBox: Property<Boolean>
}

/**
 * Injected in [modular.graphviz.tasks.GenerateGraphvizFileTask]
 */
@Suppress("UnnecessaryAbstractClass")
abstract class ExperimentalFlags @Inject constructor() {
  @get:Input abstract val adjustSvgViewBox: Property<Boolean>
}
