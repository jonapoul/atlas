/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.InternalModularApi
import modular.core.Replacement
import modular.core.internal.ChartWriter
import modular.core.internal.IndentedStringBuilder
import modular.core.internal.ModuleLink
import modular.core.internal.Subgraph
import modular.core.internal.TypedModule
import modular.core.internal.buildIndentedString
import modular.graphviz.DotConfig

@InternalModularApi
data class DotWriter(
  override val typedModules: Set<TypedModule>,
  override val links: Set<ModuleLink>,
  override val replacements: Set<Replacement>,
  override val thisPath: String,
  override val groupModules: Boolean,
  private val config: DotConfig,
) : ChartWriter() {
  override fun invoke(): String = buildIndentedString(size = 2) {
    appendLine("digraph {")
    indent {
      appendHeader()
      appendModules()
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
        "bgcolor" to config.backgroundColor,
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
      .forEach { (fromPath, toPath, _, style, color) ->
        val attrs = Attrs(
          "style" to style,
          "color" to color,
        )
        appendLine("\"$fromPath\" -> \"$toPath\"$attrs")
      }
  }

  override fun IndentedStringBuilder.appendSubgraphHeader(graph: Subgraph) {
    val cleanedModuleName = graph.name.filter { it.toString().matches(SUPPORTED_CHAR_REGEX) }
    appendLine("subgraph cluster_$cleanedModuleName {")
    indent {
      appendLine("label = \":${graph.name}\"")
    }
  }

  override fun IndentedStringBuilder.appendSubgraphFooter() {
    appendLine("}")
  }

  override fun IndentedStringBuilder.appendModule(module: TypedModule) {
    val nodePath = module.projectPath.cleaned()
    val attrs = Attrs()

    val type = module.type
    if (type != null) {
      attrs["fillcolor"] = type.color
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

  @Suppress("SpreadOperator")
  private class Attrs(private val delegate: MutableMap<String, Any?>) : MutableMap<String, Any?> by delegate {
    constructor(vararg entries: Pair<String, Any?>) : this(mutableMapOf(*entries))

    override fun toString(): String {
      if (isEmpty() || values.all { it == null }) return ""
      val csv = mapNotNull { (k, v) -> if (v == null) null else "$k=\"$v\"" }.joinToString(separator = ",")
      return " [$csv]"
    }

    fun hasAnyValues() = values.any { it != null }
  }
}
