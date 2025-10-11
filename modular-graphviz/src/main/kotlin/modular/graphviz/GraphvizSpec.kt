/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz

import modular.core.ModularDsl
import modular.core.ModularSpec
import org.gradle.api.Action
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
public interface GraphvizSpec : ModularSpec {
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

  public val node: NodeAttributes
  public fun node(action: Action<NodeAttributes>)

  public val edge: EdgeAttributes
  public fun edge(action: Action<EdgeAttributes>)

  public val graph: GraphAttributes
  public fun graph(action: Action<GraphAttributes>)
}
