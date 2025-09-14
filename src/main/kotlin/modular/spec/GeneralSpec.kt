/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import javax.inject.Inject

interface GeneralSpec {
  /**
   * When set to true, syncing the IDE (IntelliJ or Android Studio) will automatically trigger regeneration of your
   * module diagrams. Defaults to false.
   */
  val generateOnSync: Property<Boolean>

  /**
   * Use this to configure Gradle [org.gradle.api.artifacts.Configuration]s to block from consideration when collating
   * module diagrams. Defaults to ["debug", "kover", "ksp", "test"].
   */
  val ignoredConfigs: SetProperty<String>

  /**
   * Use this to block modules from inclusion in your module charts, based on their path string. E.g. a module at
   * ":path:to:my:module" will be ignored if I add `".*:to:my:.*".toRegex()` to this property.
   */
  val ignoredModules: SetProperty<Regex>

  /**
   * A separator character used internally when caching module types and links. You should only need to change this if
   * any module paths or names in configured [ModuleType]s contain a semi-colon.
   * Defaults to ";".
   */
  val separator: Property<String>

  /**
   * Set to true if you want module charts to also show modules that depend on the one in question. This will traverse
   * all the way upwards and downwards. Defaults to false.
   */
  val supportUpwardsTraversal: Property<Boolean>

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
   * modular.general.adjustSvgViewBox=true
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
  val adjustSvgViewBox: Property<Boolean>
}

/**
 * Injected in [modular.graphviz.tasks.GenerateGraphvizFileTask]
 */
@Suppress("UnnecessaryAbstractClass")
abstract class GeneralFlags @Inject constructor() {
  @get:Input abstract val adjustSvgViewBox: Property<Boolean>
  @get:Input abstract val generateOnSync: Property<Boolean>
  @get:Input abstract val ignoredConfigs: SetProperty<String>
  @get:Input abstract val ignoredModules: SetProperty<Regex>
  @get:Input abstract val separator: Property<String>
  @get:Input abstract val supportUpwardsTraversal: Property<Boolean>

  fun inject(spec: GeneralSpec) {
    adjustSvgViewBox.set(spec.adjustSvgViewBox)
    generateOnSync.set(spec.generateOnSync)
    ignoredConfigs.set(spec.ignoredConfigs)
    ignoredModules.set(spec.ignoredModules)
    separator.set(spec.separator)
    supportUpwardsTraversal.set(spec.supportUpwardsTraversal)
  }
}
