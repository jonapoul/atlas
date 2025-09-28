/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

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

internal fun buildGraphElements(
  typedModules: Set<TypedModule>,
  links: Set<ModuleLink>,
): List<GraphElement> = buildHierarchy(
  nodeData = typedModules
    .filter { module -> module in links }
    .map { module -> module to module.projectPath.split(":").filter { it.isNotEmpty() } },
)

internal fun buildHierarchy(nodeData: List<Pair<TypedModule, List<String>>>): List<GraphElement> {
  val elements = mutableListOf<GraphElement>()

  val (rootNodes, nestedNodes) = nodeData.partition { (_, parts) ->
    parts.size == 1 || parts.isEmpty()
  }

  rootNodes.forEach { (module, _) ->
    elements.add(Node(module))
  }

  nestedNodes
    .groupBy { (_, parts) -> parts.first() }
    .forEach { (firstPart, groupNodes) ->
      val subgraphElements = buildSubgraphElements(groupNodes, level = 1)
      elements.add(Subgraph(listOf(firstPart), subgraphElements))
    }

  return elements
}

internal fun buildSubgraphElements(nodeData: List<Pair<TypedModule, List<String>>>, level: Int): List<GraphElement> {
  val elements = mutableListOf<GraphElement>()

  val (leafNodes, deeperNodes) = nodeData.partition { (_, parts) ->
    parts.size == level + 1
  }

  leafNodes.forEach { (module, _) ->
    elements.add(Node(module))
  }

  // Group deeper nodes by their part at this level
  deeperNodes
    .groupBy { (_, parts) -> parts[level] }
    .forEach { (_, groupNodes) ->
      val parentPath = groupNodes.first().second.take(level + 1)
      val subgraphElements = buildSubgraphElements(groupNodes, level = level + 1)
      elements.add(Subgraph(parentPath, subgraphElements))
    }

  return elements
}
