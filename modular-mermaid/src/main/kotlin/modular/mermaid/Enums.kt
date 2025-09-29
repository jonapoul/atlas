/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused", "SpellCheckingInspection") // public API

package modular.mermaid

import modular.core.internal.StringEnum

/**
 * Elk specific option affecting how nodes are placed. Default value is [BrandesKoepf].
 * See https://mermaid.js.org/config/schema-docs/config-properties-elk.html#nodeplacementstrategy
 */
enum class NodePlacementStrategy(override val string: String) : StringEnum {
  Simple("SIMPLE"),
  NetworkSimplex("NETWORK_SIMPLEX"),
  LinearSegments("LINEAR_SEGMENTS"),
  BrandesKoepf("BRANDES_KOEPF"),
}

/**
 * This strategy decides how to find cycles in the graph and deciding which edges need adjustment to break loops.
 * Default value is [GreedyModelOrder].
 * See https://mermaid.js.org/config/schema-docs/config-properties-elk.html#cyclebreakingstrategy
 */
enum class CycleBreakingStrategy(override val string: String) : StringEnum {
  Greedy("GREEDY"),
  DepthFirst("DEPTH_FIRST"),
  Interactive("INTERACTIVE"),
  ModelOrder("MODEL_ORDER"),
  GreedyModelOrder("GREEDY_MODEL_ORDER"),
}

/**
 * Preserves the order of nodes and edges in the model file if this does not lead to additional edge crossings.
 * Depending on the strategy this is not always possible since the node and edge order might be conflicting.
 * Default value is [NodesAndEdges].
 * See https://mermaid.js.org/config/schema-docs/config-properties-elk.html#considermodelorder
 */
enum class ConsiderModelOrder(override val string: String) : StringEnum {
  None("NONE"),
  NodesAndEdges("NODES_AND_EDGES"),
  PreferEdges("PREFER_EDGES"),
  PreferNodes("PREFER_NODES"),
}

/**
 * Defaults to [Classic].
 * See https://mermaid.js.org/intro/syntax-reference.html#layout-and-look
 */
enum class Look(override val string: String) : StringEnum {
  Classic("classic"),
  HandDrawn("handDrawn"),
}

/**
 * See https://mermaid.js.org/config/theming.html
 */
enum class Theme(override val string: String) : StringEnum {
  Default("default"),
  Neutral("neutral"),
  Dark("dark"),
  Forest("forest"),
  Base("base"),
}
