/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("LongParameterList")

package modular.graphviz.internal

import modular.internal.ModuleLink
import modular.internal.Replacement
import modular.internal.TypedModule
import modular.internal.appendIndentedLine
import modular.spec.LinkType

internal class DotFileWriter(
  private val typedModules: Set<TypedModule>,
  private val links: Set<ModuleLink>,
  private val linkTypes: Set<LinkType>,
  private val replacements: Set<Replacement>,
  private val thisPath: String,
  private val arrowHead: String?,
  private val arrowTail: String?,
  private val dir: String?,
  private val dpi: Int?,
  private val fontSize: Int?,
  private val layoutEngine: String?,
  private val rankDir: String?,
  private val rankSep: Float?,
) {
  operator fun invoke(): String = buildString {
    appendLine("digraph {")
    appendHeader()
    appendNodes()
    appendLinks()
    appendLine("}")
  }

  private fun StringBuilder.appendHeader() {
    appendHeaderGroup(
      name = "edge",
      items = mapOf(
        "dir" to dir,
        "arrowhead" to arrowHead,
        "arrowtail" to arrowTail,
      ),
    )
    appendHeaderGroup(
      name = "graph",
      items = mapOf(
        "dpi" to dpi,
        "fontsize" to fontSize,
        "layout" to layoutEngine,
        "ranksep" to rankSep,
        "rankdir" to rankDir,
      ),
    )
    appendHeaderGroup(
      name = "node",
      items = mapOf("style" to "filled"),
    )
  }

  private fun StringBuilder.appendHeaderGroup(name: String, items: Map<String, Any?>) {
    val attrs = Attrs()
    items.forEach { (k, v) -> attrs[k] = v }
    if (!attrs.hasAnyValues()) return
    appendIndentedLine("$name$attrs")
  }

  private fun StringBuilder.appendLinks() {
    links
      .map { link -> link.copy(fromPath = link.fromPath.cleaned(), toPath = link.toPath.cleaned()) }
      .sortedWith(compareBy({ it.fromPath }, { it.toPath }))
      .forEach { (fromPath, toPath, configuration) ->
        val attrs = linkAttrs(configuration)
        appendIndentedLine("\"$fromPath\" -> \"$toPath\"$attrs")
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

  private fun StringBuilder.appendNodes() {
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

  private fun StringBuilder.appendNode(module: TypedModule) {
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

    appendIndentedLine("\"$nodePath\"$attrs")
  }

  private fun String.cleaned(): String {
    var string = this
    replacements.forEach { r -> string = string.replace(r.pattern, r.replacement) }
    return string
  }

  private operator fun Set<ModuleLink>.contains(module: TypedModule): Boolean =
    any { (from, to, _) -> from == module.projectPath || to == module.projectPath }

  private class Attrs : MutableMap<String, Any?> by mutableMapOf() {
    override fun toString(): String {
      if (isEmpty()) return ""
      val csv = mapNotNull { (k, v) -> if (v == null) null else "\"$k\"=\"$v\"" }.joinToString(separator = ",")
      return " [$csv]"
    }

    fun hasAnyValues() = values.any { it != null }
  }
}
