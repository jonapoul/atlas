/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import modular.core.ModularDsl

/**
 * https://mermaid.js.org/config/schema-docs/config-properties-elk.html
 */
@ModularDsl
public interface ElkLayoutSpec : MermaidLayoutSpec {
  /**
   * Elk specific option that allows edges to share path where it convenient. It can make for pretty diagrams but can
   * also make it harder to read the diagram.
   */
  public var mergeEdges: Boolean?

  /**
   * Elk specific option affecting how nodes are placed. Default value is [NodePlacementStrategy.BrandesKoepf].
   */
  public var nodePlacementStrategy: NodePlacementStrategy?

  /**
   * This strategy decides how to find cycles in the graph and deciding which edges need adjustment to break loops.
   * Default value is [CycleBreakingStrategy.GreedyModelOrder].
   *
   */
  public var cycleBreakingStrategy: CycleBreakingStrategy?

  /**
   * The node order given by the model does not change to produce a better layout. E.g. if node A is before node B in
   * the model this is not changed during crossing minimization. This assumes that the node model order is already
   * respected before crossing minimization. This can be achieved by setting [considerModelOrder] to
   * NODES_AND_EDGES.
   */
  public var forceNodeModelOrder: Boolean?

  /**
   * Preserves the order of nodes and edges in the model file if this does not lead to additional edge crossings.
   * Depending on the strategy this is not always possible since the node and edge order might be conflicting.
   * Default value is [ConsiderModelOrder.NodesAndEdges].
   */
  public var considerModelOrder: ConsiderModelOrder?
}
