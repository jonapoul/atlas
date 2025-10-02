/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi
import modular.core.spec.Replacement

@InternalModularApi
sealed interface GraphElement

@InternalModularApi
data class Node(
  val typedModule: TypedModule,
) : GraphElement

@InternalModularApi
data class Subgraph(
  val path: List<String>,
  val elements: List<GraphElement>,
) : GraphElement {
  val name = path.last()
}

@InternalModularApi
fun buildGraphElements(
  typedModules: Set<TypedModule>,
  links: Set<ModuleLink>,
): List<GraphElement> = buildHierarchy(
  nodeData = typedModules
    .filter { module -> module in links }
    .map { module -> module to module.projectPath.split(":").filter { it.isNotEmpty() } },
)

private fun buildHierarchy(nodeData: List<Pair<TypedModule, List<String>>>): List<GraphElement> {
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

private fun buildSubgraphElements(nodeData: List<Pair<TypedModule, List<String>>>, level: Int): List<GraphElement> {
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

@InternalModularApi
abstract class ChartWriter {
  protected abstract val typedModules: Set<TypedModule>
  protected abstract val links: Set<ModuleLink>
  protected abstract val replacements: Set<Replacement>
  protected abstract val groupModules: Boolean
  protected abstract val thisPath: String

  protected abstract fun IndentedStringBuilder.appendModule(module: TypedModule)
  protected abstract fun IndentedStringBuilder.appendSubgraphHeader(cleanedModuleName: String, displayName: String)
  protected abstract fun IndentedStringBuilder.appendSubgraphFooter()

  protected fun IndentedStringBuilder.appendModules() {
    if (groupModules) {
      val elements = buildGraphElements(typedModules, links)
      for (element in elements) {
        appendGraphNode(element)
      }
    } else {
      appendUngroupedNodes()
    }
  }

  private fun IndentedStringBuilder.appendGraphNode(element: GraphElement) {
    when (element) {
      is Node -> appendModule(element.typedModule)
      is Subgraph -> appendSubgraph(element)
    }
  }

  private fun IndentedStringBuilder.appendSubgraph(graph: Subgraph) {
    val cleanedName = graph.name.filter { it.toString().matches(SUPPORTED_CHAR_REGEX) }
    appendSubgraphHeader(cleanedName, graph.name)
    indent {
      for (element in graph.elements) {
        appendGraphNode(element)
      }
    }
    appendSubgraphFooter()
  }

  private fun IndentedStringBuilder.appendUngroupedNodes() {
    typedModules
      .filter { module -> module in links }
      .map { it.copy(projectPath = it.projectPath.cleaned()) }
      .sortedBy { module -> module.projectPath }
      .forEach { appendModule(it) }

    if (links.isEmpty()) {
      // Single-module case - we still want this module to be shown along with its type
      typedModules
        .firstOrNull { it.projectPath == thisPath }
        ?.let { typedModule -> appendModule(typedModule) }
    }
  }

  private fun String.cleaned(): String {
    var string = this
    replacements.forEach { r -> string = string.replace(r.pattern, r.replacement) }
    return string
  }

  private companion object {
    private val SUPPORTED_CHAR_REGEX = "^[a-zA-Z\\u0080-\\u00FF_][a-zA-Z\\u0080-\\u00FF_0-9]*$".toRegex()
  }
}

