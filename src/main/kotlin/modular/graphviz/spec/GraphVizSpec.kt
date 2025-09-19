/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.spec

import modular.core.spec.Spec
import modular.gradle.ModularDsl
import org.gradle.api.Action
import org.gradle.api.provider.Property

/**
 * Used to configure GraphViz output from Modular. For barebones output to a `.dot` file, you can just call:
 *
 * ```kotlin
 * modular {
 *   graphViz()
 * }
 * ```
 *
 * Or a more fleshed-out config:
 *
 * ```kotlin
 * modular {
 *   graphViz {
 *     pathToDotCommand = "/custom/path/to/dot"
 *
 *     fileFormats {
 *       png()
 *       svg()
 *     }
 *
 *     ...
 *   }
 * }
 * ```
 */
interface GraphVizSpec : Spec {
  /**
   * Use this if you want to specify a "dot" command which isn't on the system path. This should be an absolute path.
   */
  val pathToDotCommand: Property<String>

  /**
   * Manually interact with output formats from GraphViz. Defaults to an empty set, meaning the only output will
   * be a `.dot` file.
   *
   * See [GraphVizFileFormatSpec] for some default options, but bear in mind your machine may not have them all
   * available (depending on installedGraphViz version).
   */
  val fileFormats: GraphVizFileFormatSpec
  @ModularDsl fun fileFormats(action: Action<GraphVizFileFormatSpec>)

  /**
   * Configure the arrow type from a module pointing to its dependency. Defaults to unset, so GraphViz will fall back
   * to [ArrowType.Normal]. Default arrows are specified in the [ArrowType] enum, but if you have any extras available
   * you can pass them into [arrowHead] as a string instead.
   * See https://graphviz.org/docs/attrs/arrowhead/
   */
  val arrowHead: Property<String>
  @ModularDsl fun arrowHead(type: ArrowType)
  @ModularDsl fun arrowHead(type: String)

  /**
   * Configure the arrow type from a dependency pointing to its dependent. Defaults to unset, so GraphViz will fall back
   * to [ArrowType.None]. Default arrows are specified in the [ArrowType] enum, but if you have any extras available
   * you can pass them into [arrowTail] as a string instead.
   * See https://graphviz.org/docs/attrs/arrowtail/
   */
  val arrowTail: Property<String>
  @ModularDsl fun arrowTail(type: ArrowType)
  @ModularDsl fun arrowTail(type: String)

  /**
   * Customise the layout engine used to organise your module nodes in the chart. Defaults to [LayoutEngine.Dot].
   * Default engines are specified in the [LayoutEngine] enum, but if you have any extras available you can pass them
   * into [layoutEngine] as a string instead.
   * See https://graphviz.org/docs/layouts/
   */
  val layoutEngine: Property<String>
  @ModularDsl fun layoutEngine(layoutEngine: LayoutEngine)
  @ModularDsl fun layoutEngine(layoutEngine: String)

  /**
   * Specifies the expected number of pixels per inch on a display device. Defaults to unset, but GraphViz will fall
   * back to 96 internally.
   * See https://graphviz.org/docs/attrs/dpi/
   */
  val dpi: Property<Int>

  /**
   * Font size, in points, used for text. Unset by default, but GraphViz will fall back to 14.0 internally.
   * See https://graphviz.org/docs/attrs/fontsize/
   */
  val fontSize: Property<Int>

  /**
   * Sets direction of graph layout. Unset by default, but GraphViz will fall back to [RankDir.TopToBottom] internally.
   * See https://graphviz.org/docs/attrs/rankdir/
   */
  val rankDir: Property<String>
  @ModularDsl fun rankDir(rankDir: RankDir)
  @ModularDsl fun rankDir(rankDir: String)

  /**
   * Specifies separation between ranks in the chart. Unset by default, GraphViz will use a different default value
   * depending on your [layoutEngine] value. 0.5 for [LayoutEngine.Dot], 1.0 for [LayoutEngine.TwoPi].
   * See https://graphviz.org/docs/attrs/ranksep/
   */
  val rankSep: Property<Float>

  /**
   * Edge type for drawing arrowheads. Unset by default, GraphViz will
   * See https://graphviz.org/docs/attrs/dir/
   */
  val dir: Property<String>
  @ModularDsl fun dir(dir: Dir)
  @ModularDsl fun dir(dir: String)
}
