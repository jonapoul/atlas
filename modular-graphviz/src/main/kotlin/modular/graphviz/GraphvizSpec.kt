/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz

import modular.core.ModularDsl
import modular.core.ModularSpec
import org.gradle.api.provider.Property

/**
 * Used to configure Graphviz output from Modular. For barebones output to a `.dot` file, you can just add the
 * `"dev.jonpoulton.modular.graphviz"` gradle plugin. Or for a more fleshed-out config:
 *
 * ```kotlin
 * modular {
 *   // other Modular config
 *
 *   graphviz {
 *     pathToDotCommand = "/custom/path/to/dot"
 *     fileFormat = FileFormat.Svg
 *
 *     ...
 *   }
 * }
 * ```
 */
@ModularDsl
interface GraphvizSpec : ModularSpec {
  /**
   * When enabled, the generated Graphviz SVG file will have its `viewBox` parameter auto-adjusted to make sure all the
   * contents fit in the visible frame. This is to work around an issue I've seen where the custom DPI value tends to
   * make the bottom right of the chart clipped out of the view box.
   *
   * Not necessary unless you have:
   * a) configured Graphviz as an output
   * b) have specified SVG output file for Graphviz
   * c) have configured a custom DPI value for Graphviz
   *
   * You can either enable this flag in the Gradle script, or set the following in your `gradle.properties` file:
   *
   * ```properties
   * modular.graphviz.adjustSvgViewBox=true
   * ```
   *
   * Disabled by default.
   *
   * If you don't want this adjustment to happen at all, you can suppress the Gradle warning with the following in
   * `gradle.properties`:
   *
   * ```properties
   * modular.graphviz.suppressAdjustSvgViewBox=true
   * ```
   */
  val adjustSvgViewBox: Property<Boolean>

  /**
   * Use this if you want to specify a "dot" command which isn't on the system path. This should be an absolute path.
   */
  val pathToDotCommand: Property<String>

  /**
   * Set the background color of the generated image. Set to `"transparent"` for no background.
   * See https://graphviz.org/docs/attrs/bgcolor/
   */
  val backgroundColor: Property<String>

  /**
   * Manually interact with output formats from Graphviz. Defaults to an empty set, meaning the only output will
   * be a `.dot` file.
   *
   * See [FileFormat] for some default options, but bear in mind your machine may not have them all
   * available (depending on your installed Graphviz version).
   */
  val fileFormat: Property<FileFormat>

  /**
   * Configure the arrow type from a module pointing to its dependency. Defaults to unset, so Graphviz will fall back
   * to [ArrowType.Normal]. Default arrows are specified in the [ArrowType] enum, but if you have any extras available
   * you can pass them into [arrowHead] as a string instead.
   * See https://graphviz.org/docs/attrs/arrowhead/
   */
  val arrowHead: Property<ArrowType>

  /**
   * Configure the arrow type from a dependency pointing to its dependent. Defaults to unset, so Graphviz will fall back
   * to [ArrowType.None]. Default arrows are specified in the [ArrowType] enum, but if you have any extras available
   * you can pass them into [arrowTail] as a string instead.
   * See https://graphviz.org/docs/attrs/arrowtail/
   */
  val arrowTail: Property<ArrowType>

  /**
   * Customise the layout engine used to organise your module nodes in the chart. Defaults to [LayoutEngine.Dot].
   * Default engines are specified in the [LayoutEngine] enum, but if you have any extras available you can pass them
   * into [layoutEngine] as a string instead.
   * See https://graphviz.org/docs/layouts/
   */
  val layoutEngine: Property<LayoutEngine>

  /**
   * Specifies the expected number of pixels per inch on a display device. Defaults to unset, but Graphviz will fall
   * back to 96 internally.
   * See https://graphviz.org/docs/attrs/dpi/
   */
  val dpi: Property<Int>

  /**
   * Font size, in points, used for text. Unset by default, but Graphviz will fall back to 14.0 internally.
   * See https://graphviz.org/docs/attrs/fontsize/
   */
  val fontSize: Property<Int>

  /**
   * Sets direction of graph layout. Unset by default, but Graphviz will fall back to [RankDir.TopToBottom] internally.
   * See https://graphviz.org/docs/attrs/rankdir/
   */
  val rankDir: Property<RankDir>

  /**
   * Specifies separation between ranks in the chart. Unset by default, Graphviz will use a different default value
   * depending on your [layoutEngine] value. 0.5 for [LayoutEngine.Dot], 1.0 for [LayoutEngine.TwoPi].
   * See https://graphviz.org/docs/attrs/ranksep/
   */
  val rankSep: Property<Float>

  /**
   * Edge type for drawing arrowheads. Unset by default, Graphviz will
   * See https://graphviz.org/docs/attrs/dir/
   */
  val dir: Property<Dir>
}
