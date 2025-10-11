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
public data class DotWriter(
  override val typedModules: Set<TypedModule>,
  override val links: Set<ModuleLink>,
  override val replacements: Set<Replacement>,
  override val thisPath: String,
  override val groupModules: Boolean,
  private val config: DotConfig,
) : ChartWriter() {
  override fun invoke(): String = buildIndentedString {
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
      attrs = attrs(config.edgeAttributes),
    )
    appendHeaderGroup(
      name = "graph",
      attrs = Attrs("layout" to config.layoutEngine) + config.graphAttributes,
    )
    appendHeaderGroup(
      name = "node",
      attrs = attrs(config.nodeAttributes),
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
      .forEach { (fromPath, toPath, _, type) ->
        val attrs = Attrs(
          "style" to type?.style,
          "color" to type?.color,
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

    module.type?.let { type ->
      val properties = type.properties + ("fillcolor" to type.color)
      properties.forEach { (key, value) -> attrs[key] = value }
    }

    appendLine("\"$nodePath\"$attrs")
  }

  private fun attrs(map: Map<String, String>?) = Attrs(map.orEmpty().toMutableMap())

  @Suppress("SpreadOperator")
  private class Attrs(private val delegate: MutableMap<String, Any?>) : MutableMap<String, Any?> by delegate {
    constructor(vararg entries: Pair<String, Any?>) : this(mutableMapOf(*entries))

    override fun toString(): String {
      if (isEmpty() || values.all { it == null }) return ""
      val csv = mapNotNull { (k, v) -> if (v == null) null else "$k=\"$v\"" }.joinToString(separator = ",")
      return " [$csv]"
    }

    public fun hasAnyValues() = values.any { it != null }

    operator fun plus(other: Map<String, String>?): Attrs {
      other?.let(delegate::putAll)
      return Attrs(delegate)
    }
  }
}
