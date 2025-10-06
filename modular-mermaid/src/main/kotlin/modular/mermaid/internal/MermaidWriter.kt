/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.core.LinkStyle
import modular.core.Replacement
import modular.core.internal.ChartWriter
import modular.core.internal.IndentedStringBuilder
import modular.core.internal.ModuleLink
import modular.core.internal.Subgraph
import modular.core.internal.TypedModule
import modular.core.internal.buildIndentedString
import modular.core.internal.contains
import modular.mermaid.MermaidConfig

internal class MermaidWriter(
  override val typedModules: Set<TypedModule>,
  override val links: Set<ModuleLink>,
  override val replacements: Set<Replacement>,
  override val thisPath: String,
  override val groupModules: Boolean,
  private val config: MermaidConfig,
) : ChartWriter() {
  override fun invoke(): String = buildIndentedString {
    appendConfig()
    appendLine("graph TD")
    indent {
      appendModules()
      appendTypes()
      appendLinks()
    }
  }

  private fun IndentedStringBuilder.appendConfig() {
    appendLine("---")
    appendLine("config:")
    indent {
      val layout = config.layout
      layout?.let { appendLine("layout: $it") }
      config.look?.let { appendLine("look: $it") }
      config.theme?.let { appendLine("theme: $it") }
      if (layout != null) appendProperties(name = layout, config.layoutProperties)
      appendProperties(name = "themeVariables", config.themeVariables)
    }
    appendLine("---")
  }

  private fun IndentedStringBuilder.appendProperties(name: String, properties: Map<String, String>?) {
    if (properties.isNullOrEmpty()) return
    appendLine("$name:")
    indent {
      for ((key, value) in properties) {
        appendLine("$key: $value")
      }
    }
  }

  override fun IndentedStringBuilder.appendSubgraphHeader(graph: Subgraph) {
    val cleanedModuleName = graph.name.filter { it.toString().matches(SUPPORTED_CHAR_REGEX) }
    appendLine("subgraph $cleanedModuleName[\"${graph.name}\"]")
  }

  override fun IndentedStringBuilder.appendSubgraphFooter() {
    appendLine("end")
  }

  override fun IndentedStringBuilder.appendModule(module: TypedModule) {
    appendLine("${module.label}[\"${module.projectPath.cleaned()}\"]")
  }

  private fun IndentedStringBuilder.appendTypes() {
    for (module in typedModules) {
      if (module !in links && module.projectPath != thisPath) continue

      val attrs = Attrs(
        "fill" to module.type?.color,
        "color" to "black",
      )

      if (thisPath == module.projectPath) {
        attrs["font-weight"] = "bold"
        attrs["stroke"] = "black"
        attrs["stroke-width"] = "2px"
      }
      appendLine("style ${module.label} $attrs")
    }
  }

  // See https://mermaid.js.org/syntax/flowchart.html#links-between-nodes
  private fun IndentedStringBuilder.appendLinks() {
    links.forEachIndexed { i, link ->
      val arrowPrefix = if (config.animateLinks) "link$i@" else ""
      val arrow = when (link.type?.style) {
        LinkStyle.Bold -> "==>"
        LinkStyle.Dashed -> "-.->"
        LinkStyle.Dotted -> "-.->"
        LinkStyle.Invis -> "~~~"
        LinkStyle.Solid -> "-->"
        else -> "-->"
      }
      appendLine("${link.fromPath.label} $arrowPrefix$arrow ${link.toPath.label}")
      link.type?.color?.let { color -> appendLine("linkStyle $i stroke:$color") }
    }

    if (config.animateLinks) {
      for (i in links.indices) {
        appendLine("link$i@{ animate: true }")
      }
    }
  }

  private val TypedModule.label: String
    get() = projectPath.label

  private val String.label: String
    get() = split(":", "-", " ").joinToString("_")

  @Suppress("SpreadOperator")
  private class Attrs private constructor(
    private val delegate: MutableMap<String, Any?>,
  ) : MutableMap<String, Any?> by delegate {
    constructor(vararg entries: Pair<String, Any?>) : this(mutableMapOf(*entries))

    override fun toString(): String {
      if (values.filterNotNull().isEmpty()) return ""
      return mapNotNull { (k, v) -> if (v == null) null else "$k:$v" }.joinToString(separator = ",")
    }
  }
}
