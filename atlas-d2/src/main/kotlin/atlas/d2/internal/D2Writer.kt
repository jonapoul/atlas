@file:Suppress("TooManyFunctions", "LongParameterList")

package atlas.d2.internal

import atlas.core.InternalAtlasApi
import atlas.core.LinkType
import atlas.core.ModuleType
import atlas.core.Replacement
import atlas.core.internal.ChartWriter
import atlas.core.internal.IndentedStringBuilder
import atlas.core.internal.ModuleLink
import atlas.core.internal.Subgraph
import atlas.core.internal.TypedModule
import atlas.core.internal.buildIndentedString
import atlas.core.internal.contains

@InternalAtlasApi
public class D2Writer(
  override val typedModules: Set<TypedModule>,
  override val links: Set<ModuleLink>,
  override val replacements: Set<Replacement>,
  override val thisPath: String,
  override val groupModules: Boolean,
  private val pathToClassesFile: String,
) : ChartWriter() {
  private var subgraphNestingLevel = 0

  override fun invoke(): String = buildIndentedString {
    appendImports()
    appendModules()
    appendLinks()
    appendLegend()
  }

  private fun IndentedStringBuilder.appendImports() {
    appendLine("...@$pathToClassesFile")
  }

  override fun IndentedStringBuilder.appendSubgraphHeader(graph: Subgraph) {
    val key = graph.path[subgraphNestingLevel].cleaned()
    appendLine("$key: :${graph.name} {")
    indent { appendLine("class: $CONTAINER_CLASS") }
    subgraphNestingLevel++
  }

  override fun IndentedStringBuilder.appendSubgraphFooter() {
    appendLine("}")
    subgraphNestingLevel--
  }

  override fun IndentedStringBuilder.appendModule(module: TypedModule) {
    val (path, type) = module
    val key = path.localKey()
    val name = if (groupModules) ":$key" else path
    if (type == null) {
      // just list the module with label
      appendLine("$key: $name")
    } else {
      // also include the class key
      appendLine("$key: $name { class: ${type.classId} }")
    }
  }

  private fun IndentedStringBuilder.appendLinks() {
    links
      .map { l -> l.copy(fromPath = l.fromPath.cleaned(), toPath = l.toPath.cleaned()) }
      .sortedWith(compareBy({ it.fromPath.fullKey() }, { it.toPath.fullKey() }))
      .forEach { l ->
        val suffix = l.type?.let { type -> " { class: ${type.classId} }" }.orEmpty()
        appendLine("${l.fromPath.fullKey()} -> ${l.toPath.fullKey()}$suffix")
      }
  }

  private fun IndentedStringBuilder.appendLegend() {
    val moduleTypes = typedModules
      .filter { it in links }
      .mapNotNull { it.type }
      .distinct()
      .ifEmpty {
        // single-module case
        typedModules
          .firstOrNull { it.projectPath == thisPath }
          ?.type
          ?.let(::listOf)
          .orEmpty()
      }

    val linkTypes = links
      .mapNotNull { it.type }
      .distinct()

    if (moduleTypes.isEmpty() && linkTypes.isEmpty()) return

    appendLine("vars: {")
    indent {
      appendLine("d2-legend: {")
      indent {
        moduleTypes.forEach { appendModuleType(it) }
        if (linkTypes.isNotEmpty()) {
          val exampleNodes = getExampleNodesForLinks()
          linkTypes.forEach { appendLinkType(it, exampleNodes) }
        }
      }
      appendLine("}")
    }
    appendLine("}")
  }

  private fun IndentedStringBuilder.getExampleNodesForLinks(): Pair<ModuleType, ModuleType> {
    val dummy = ModuleType(name = "dummy1", color = "", properties = emptyMap())
    val dummy2 = ModuleType(name = "dummy2", color = "", properties = emptyMap())
    appendLine("${dummy.classId}.class: $HIDDEN_CLASS")
    appendLine("${dummy2.classId}.class: $HIDDEN_CLASS")
    return dummy to dummy2
  }

  private fun IndentedStringBuilder.appendModuleType(type: ModuleType) {
    appendLine("${type.classId}: ${type.name} { class: ${type.classId} }")
  }

  private fun IndentedStringBuilder.appendLinkType(type: LinkType, exampleNodes: Pair<ModuleType, ModuleType>) {
    val (a, b) = exampleNodes
    appendLine("${a.classId} -> ${b.classId}: ${type.displayName} { class: ${type.classId} }")
  }

  // Colons in d2 have special meaning, so strip them out of the key string. Not visible in the diagram anyway.
  private fun String.fullKey(): String {
    val stripped = DISALLOWED_SUBSTRINGS.fold(
      initial = cleaned()
        .removePrefix(":")
        .removePrefix("-"),
    ) { acc, string -> acc.replace(string, "") }

    return if (groupModules) {
      // Dots are nested group identifiers, so only use them if we have grouping enabled
      stripped.replace(":", ".")
    } else {
      // otherwise just use underscores, so long as they're unique it shouldn't matter
      stripped.replace(":", "_")
    }
  }

  private fun String.localKey(): String = if (groupModules) {
    // "path.to.my.module" -> "module" for subgraphNestingLevel=3
    fullKey().split(".")[subgraphNestingLevel]
  } else {
    fullKey()
  }

  private companion object {
    val DISALLOWED_SUBSTRINGS = setOf("{", "}", "->", "--", "<-", "<->", "|", "#", "\"", "'")
  }
}
