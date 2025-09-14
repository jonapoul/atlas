/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.graphviz.spec.DotFileConfig
import modular.internal.GraphElement
import modular.internal.IndentedStringBuilder
import modular.internal.ModuleLink
import modular.internal.Node
import modular.internal.Replacement
import modular.internal.Subgraph
import modular.internal.TypedModule
import modular.internal.buildGraphElements
import modular.internal.buildIndentedString
import modular.spec.LinkType

internal class DotFileWriter(
  private val typedModules: Set<TypedModule>,
  private val links: Set<ModuleLink>,
  private val linkTypes: Set<LinkType>,
  private val replacements: Set<Replacement>,
  private val thisPath: String,
  private val groupModules: Boolean,
  private val config: DotFileConfig,
) {
  operator fun invoke(): String = buildIndentedString(size = 2) {
    appendLine("digraph {")
    indent {
      appendHeader()
      appendNodes()
      appendLinks()
    }
    appendLine("}")
  }

  private fun IndentedStringBuilder.appendHeader() {
    appendHeaderGroup(
      name = "edge",
      attrs = Attrs(
        "dir" to config.dir,
        "arrowhead" to config.arrowHead,
        "arrowtail" to config.arrowTail,
      ),
    )
    appendHeaderGroup(
      name = "graph",
      attrs = Attrs(
        "dpi" to config.dpi,
        "fontsize" to config.fontSize,
        "layout" to config.layoutEngine,
        "ranksep" to config.rankSep,
        "rankdir" to config.rankDir,
      ),
    )
    appendHeaderGroup(
      name = "node",
      attrs = Attrs(
        "style" to "filled",
      ),
    )
  }

  private fun IndentedStringBuilder.appendHeaderGroup(name: String, attrs: Attrs) {
    if (!attrs.hasAnyValues()) return
    appendLine("$name$attrs")
  }

  private fun IndentedStringBuilder.appendLinks() {
    links
      .map { link -> link.copy(fromPath = link.fromPath.cleaned(), toPath = link.toPath.cleaned()) }
      .sortedWith(compareBy({ it.fromPath }, { it.toPath }))
      .forEach { (fromPath, toPath, configuration) ->
        val attrs = linkAttrs(configuration)
        appendLine("\"$fromPath\" -> \"$toPath\"$attrs")
      }
  }

  private fun linkAttrs(configuration: String): Attrs {
    val type = linkTypes
      .firstOrNull { s -> s.configuration.matches(configuration) }
      ?: return Attrs()

    val attrs = Attrs()
    attrs["style"] = type.style
    attrs["color"] = type.color
    return attrs
  }

  private fun IndentedStringBuilder.appendNodes() {
    if (groupModules) {
      val elements = buildGraphElements(typedModules)
      for (element in elements) {
        appendGraphNode(element)
      }
    } else {
      appendUngroupedNodes()
    }
  }

  private fun IndentedStringBuilder.appendGraphNode(element: GraphElement) {
    when (element) {
      is Node -> appendNode(element.typedModule)
      is Subgraph -> appendSubgraph(element)
    }
  }

  private fun IndentedStringBuilder.appendSubgraph(graph: Subgraph) {
    appendLine("subgraph cluster_${graph.name} {")
    indent {
      appendLine("label = \":${graph.name}\"")
      for (element in graph.elements) {
        appendGraphNode(element)
      }
    }
    appendLine("}")
  }

  private fun IndentedStringBuilder.appendUngroupedNodes() {
    typedModules
      .filter { module -> module in links }
      .map { it.copy(projectPath = it.projectPath.cleaned()) }
      .sortedBy { module -> module.projectPath }
      .forEach { appendNode(it) }

    if (links.isEmpty()) {
      // Single-module case - we still want this module to be shown along with its type
      typedModules
        .firstOrNull { it.projectPath == thisPath }
        ?.let { typedModule -> appendNode(typedModule) }
    }
  }

  private fun IndentedStringBuilder.appendNode(module: TypedModule) {
    val nodePath = module.projectPath.cleaned()
    val attrs = Attrs()

    if (module.type != null) {
      attrs["fillcolor"] = module.type.color
    }

    // Make "target" nodes more prominent with a thick black border
    if (thisPath.cleaned() == nodePath) {
      attrs["penwidth"] = "3"
      attrs["shape"] = "box"
    } else {
      attrs["shape"] = "none"
    }

    appendLine("\"$nodePath\"$attrs")
  }

  private fun String.cleaned(): String {
    var string = this
    replacements.forEach { r -> string = string.replace(r.pattern, r.replacement) }
    return string
  }

  private operator fun Set<ModuleLink>.contains(module: TypedModule): Boolean =
    any { (from, to, _) -> from == module.projectPath || to == module.projectPath }

  @Suppress("SpreadOperator")
  private class Attrs(private val delegate: MutableMap<String, Any?>) : MutableMap<String, Any?> by delegate {
    constructor(vararg entries: Pair<String, Any?>) : this(mutableMapOf(*entries))

    override fun toString(): String {
      if (isEmpty()) return ""
      val csv = mapNotNull { (k, v) -> if (v == null) null else "\"$k\"=\"$v\"" }.joinToString(separator = ",")
      return " [$csv]"
    }

    fun hasAnyValues() = values.any { it != null }
  }
}
