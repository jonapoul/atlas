package atlas.core.internal

import atlas.core.InternalAtlasApi
import atlas.core.Replacement

@InternalAtlasApi
public sealed interface GraphElement

@InternalAtlasApi
public data class Node(
  val typedModule: TypedModule,
) : GraphElement

@InternalAtlasApi
public data class Subgraph(
  val path: List<String>,
  val elements: List<GraphElement>,
) : GraphElement {
  public val name: String = path.last()
}

@InternalAtlasApi
public fun buildGraphElements(
  typedModules: Set<TypedModule>,
  links: Set<ModuleLink>,
  thisPath: String,
): List<GraphElement> = buildHierarchy(
  nodeData = typedModules
    .filter { module -> module in links || (links.isEmpty() && module.projectPath == thisPath) }
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

@InternalAtlasApi
public abstract class ChartWriter {
  public abstract operator fun invoke(): String

  protected abstract val typedModules: Set<TypedModule>
  protected abstract val links: Set<ModuleLink>
  protected abstract val replacements: Set<Replacement>
  protected abstract val groupModules: Boolean
  protected abstract val thisPath: String

  protected abstract fun IndentedStringBuilder.appendModule(module: TypedModule)
  protected abstract fun IndentedStringBuilder.appendSubgraphHeader(graph: Subgraph)
  protected abstract fun IndentedStringBuilder.appendSubgraphFooter()

  protected fun IndentedStringBuilder.appendModules() {
    if (groupModules) {
      val elements = buildGraphElements(typedModules, links, thisPath)
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
    appendSubgraphHeader(graph)
    indent {
      for (element in graph.elements) {
        appendGraphNode(element)
      }
    }
    appendSubgraphFooter()
  }

  protected fun IndentedStringBuilder.appendUngroupedNodes() {
    typedModules
      .filter { it in links }
      .map { it.cleaned() }
      .sortedBy { it.projectPath }
      .forEach { appendModule(it) }

    if (links.isEmpty()) {
      // Single-module case - we still want this module to be shown along with its type
      typedModules
        .firstOrNull { it.projectPath == thisPath }
        ?.let { appendModule(it.cleaned()) }
    }
  }

  protected fun String.cleaned(): String {
    var string = this
    replacements.forEach { r -> string = string.replace(r.pattern, r.replacement) }
    return string
  }

  protected fun TypedModule.cleaned(): TypedModule = copy(projectPath = projectPath.cleaned())

  @InternalAtlasApi
  public companion object {
    @InternalAtlasApi
    public val SUPPORTED_CHAR_REGEX: Regex = "^[a-zA-Z\\u0080-\\u00FF_][a-zA-Z\\u0080-\\u00FF_0-9]*$".toRegex()
  }
}
