/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("LongParameterList")

package modular.graphviz.internal

import modular.internal.ModuleLink
import modular.internal.Replacement
import modular.internal.TypedModule
import modular.internal.appendIndented
import modular.internal.appendIndentedLine

/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
internal class DotFileWriter(
  private val typedModules: Set<TypedModule>,
  private val links: Set<ModuleLink>,
  private val replacements: Set<Replacement>,
  private val thisPath: String,
  private val arrowHead: String?,
  private val arrowTail: String?,
  private val dir: String?,
  private val dpi: Int?,
  private val fontSize: Int?,
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
    appendIndented("$name [")
    val itemsString = items
      .toList()
      .mapNotNull { (k, v) -> if (v == null) null else "\"$k\"=\"$v\"" }
      .joinToString(separator = ",")
    append(itemsString)
    append("]")
    appendLine()
  }

  private fun StringBuilder.appendLinks() {
    links
      .map { link -> link.copy(fromPath = link.fromPath.cleaned(), toPath = link.toPath.cleaned()) }
      .sortedWith(compareBy({ it.fromPath }, { it.toPath }))
      .forEach { (fromPath, toPath, configuration) ->
        val attrs = if (configuration.contains("implementation", ignoreCase = true)) {
          " [\"style\"=\"dotted\"]"
        } else {
          ""
        }
        appendIndentedLine("\"$fromPath\" -> \"$toPath\"$attrs")
      }
  }

  private fun StringBuilder.appendNodes() {
    typedModules
      .filter { module -> module in links }
      .map { it.copy(projectPath = it.projectPath.cleaned()) }
      .sortedBy { module -> module.projectPath }
      .forEach { appendNode(it) }

    if (links.isEmpty()) {
      // Single-module case - we still want this module to be shown along with its type
      val typedModule = typedModules
        .firstOrNull { it.projectPath == thisPath }
        ?: error("No type found for $thisPath")
      appendNode(typedModule)
    }
  }

  private fun StringBuilder.appendNode(module: TypedModule) {
    val path = module.projectPath.cleaned()
    val attrs = if (thisPath.cleaned() == path) {
      "\"color\"=\"black\",\"penwidth\"=\"3\",\"shape\"=\"box\""
    } else {
      "\"shape\"=\"none\""
    }
    appendIndentedLine("\"$path\" [\"fillcolor\"=\"${module.type.color}\",$attrs]")
  }

  private fun String.cleaned(): String {
    var string = this
    replacements.forEach { r -> string = string.replace(r.pattern, r.replacement) }
    return string
  }

  private operator fun Set<ModuleLink>.contains(module: TypedModule): Boolean =
    any { (from, to, _) -> from == module.projectPath || to == module.projectPath }
}
