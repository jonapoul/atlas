/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.graphviz.spec

import modular.gradle.ModularDsl
import modular.spec.Spec
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
 *     fileExtension = "dot"
 *     pathToDotCommand = "/custom/path/to/dot"
 *
 *     legend()
 *
 *     fileFormats {
 *       png()
 *       svg()
 *     }
 *
 *     chart {
 *       ...
 *     }
 *
 *     linkTypes {
 *       api(style = LinkStyle.Bold)
 *       implementation(color = "red")
 *     }
 *   }
 * }
 * ```
 */
interface GraphVizSpec : Spec<GraphVizLegendSpec, GraphVizChartSpec> {
  /**
   * To configure the file extension of generated dotfiles. Defaults to "dot".
   */
  override val fileExtension: Property<String>

  /**
   * Use this if you want to specify a "dot" command which isn't on the system path. This should be an absolute path.
   */
  val pathToDotCommand: Property<String>

  /**
   * Use this to manually configure the [GraphVizLegendSpec], or set it to null if you want to explicitly disable
   * legend generation. Defaults to null.
   */
  override var legend: GraphVizLegendSpec?
  override fun legend(): GraphVizLegendSpec
  override fun legend(action: Action<GraphVizLegendSpec>)

  /**
   * Call this to configure the chart contents, orientation, font size, arrows, etc. Example:
   * ```kotlin
   * modular {
   *   chart {
   *     arrowHead = "diamond"
   *     dpi = 150
   *     fontSize = 25
   *     rankSep = 4.0
   *   }
   * }
   * ```
   *
   * Not required - the chart will be generated with default settings without calling this.
   */
  override val chart: GraphVizChartSpec

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
   * Use to configure link styles between each module in the diagram. Empty by default, so all links will have a solid
   * black line.
   */
  val linkTypes: GraphVizLinkTypesSpec
  @ModularDsl fun linkTypes(action: Action<GraphVizLinkTypesSpec>)
}
