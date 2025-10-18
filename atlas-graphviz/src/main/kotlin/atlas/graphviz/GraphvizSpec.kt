/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.graphviz

import atlas.core.AtlasDsl
import atlas.core.AtlasSpec
import org.gradle.api.Action
import org.gradle.api.provider.Property

/**
 * Used to configure Graphviz output from Atlas. For barebones output to a `.dot` file, you can just add the
 * `"dev.jonpoulton.atlas.graphviz"` gradle plugin. Or for a more fleshed-out config:
 *
 * ```kotlin
 * atlas {
 *   // other Atlas config
 *
 *   graphviz {
 *     pathToDotCommand = "/custom/path/to/dot"
 *     fileFormat = FileFormat.Svg
 *     layoutEngine = LayoutEngine.Dot
 *
 *     node {
 *       ...
 *     }
 *
 *     edge {
 *       ...
 *     }
 *
 *     graph {
 *       ...
 *     }
 *   }
 * }
 * ```
 */
@AtlasDsl
public interface GraphvizSpec : AtlasSpec {
  /**
   * Use this if you want to specify a "dot" command which isn't on the system path. This should be an absolute path.
   */
  public val pathToDotCommand: Property<String>

  /**
   * Manually interact with output formats from Graphviz. Defaults to an empty set, meaning the only output will
   * be a `.dot` file.
   *
   * See [FileFormat] for some default options, but bear in mind your machine may not have them all
   * available (depending on your installed Graphviz version).
   */
  public val fileFormat: Property<FileFormat>

  /**
   * Customise the layout engine used to organise your module nodes in the chart. Defaults to [LayoutEngine.Dot].
   * Default engines are specified in the [LayoutEngine] enum, but if you have any extras available you can pass them
   * into [layoutEngine] as a string instead.
   * See https://graphviz.org/docs/layouts/
   */
  public val layoutEngine: Property<LayoutEngine>

  /**
   * Configure the attributes applied by default to all module nodes, unless overridden by that node's
   * [GraphvizModuleTypeSpec].
   */
  public val node: NodeAttributes
  public fun node(action: Action<NodeAttributes>)

  /**
   * Configure the attributes applied by default to all links between nodes, unless overridden by that link's
   * [GraphvizLinkTypeSpec].
   */
  public val edge: EdgeAttributes
  public fun edge(action: Action<EdgeAttributes>)

  /**
   * Configure the attributes applied to the root graph.
   */
  public val graph: GraphAttributes
  public fun graph(action: Action<GraphAttributes>)
}
