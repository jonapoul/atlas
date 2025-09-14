/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

internal sealed interface GraphElement

internal data class Node(
  val typedModule: TypedModule,
) : GraphElement

internal data class Subgraph(
  val path: List<String>,
  val elements: List<GraphElement>,
) : GraphElement {
  val name = path.last()
}

internal fun buildGraphElements(typedModules: Set<TypedModule>): List<GraphElement> = buildHierarchy(
  nodeData = typedModules.map { module ->
    module to module.projectPath.split(":").filter { it.isNotEmpty() }
  },
)

internal fun buildHierarchy(nodeData: List<Pair<TypedModule, List<String>>>): List<GraphElement> {
  val elements = mutableListOf<GraphElement>()

  // Separate root nodes (single part or "app") from nested nodes
  val (rootNodes, nestedNodes) = nodeData.partition { (_, parts) ->
    parts.size == 1 || parts.isEmpty()
  }

  // Add root nodes directly
  rootNodes.forEach { (module, _) ->
    elements.add(Node(module))
  }

  // Group nested nodes by their first part
  val grouped = nestedNodes.groupBy { (_, parts) -> parts.first() }

  // Create subgraphs for each group
  grouped.forEach { (firstPart, groupNodes) ->
    val subgraphElements = buildSubgraphElements(groupNodes, level = 1)
    elements.add(Subgraph(listOf(firstPart), subgraphElements))
  }

  return elements
}

internal fun buildSubgraphElements(nodeData: List<Pair<TypedModule, List<String>>>, level: Int): List<GraphElement> {
  val elements = mutableListOf<GraphElement>()

  // Separate nodes that end at this level from those that go deeper
  val (leafNodes, deeperNodes) = nodeData.partition { (_, parts) ->
    parts.size == level + 1
  }

  // Add leaf nodes
  leafNodes.forEach { (module, _) ->
    elements.add(Node(module))
  }

  // Group deeper nodes by their part at this level
  val grouped = deeperNodes.groupBy { (_, parts) -> parts[level] }

  // Create nested subgraphs
  grouped.forEach { (_, groupNodes) ->
    val parentPath = groupNodes.first().second.take(level + 1)
    val subgraphElements = buildSubgraphElements(groupNodes, level = level + 1)
    elements.add(Subgraph(parentPath, subgraphElements))
  }

  return elements
}
