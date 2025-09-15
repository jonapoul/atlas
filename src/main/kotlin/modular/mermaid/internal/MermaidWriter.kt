/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.internal.IndentedStringBuilder
import modular.internal.ModuleLink
import modular.internal.Replacement
import modular.internal.TypedModule
import modular.internal.buildIndentedString
import modular.mermaid.spec.MermaidConfig
import modular.internal.contains

internal class MermaidWriter(
  private val typedModules: Set<TypedModule>,
  private val links: Set<ModuleLink>,
  private val replacements: Set<Replacement>,
  private val thisPath: String,
  private val groupModules: Boolean,
  private val config: MermaidConfig,
) {
  operator fun invoke(): String = buildIndentedString(size = 2) {
    appendConfig()
    appendLine("graph TD")
    indent {
      appendModules()
      appendTypes()
      appendLinks()
    }
  }

  private fun IndentedStringBuilder.appendConfig() {
    val layout = config.layout ?: return
    val properties = config.layoutProperties ?: emptyMap()
    appendLine("---")
    appendLine("config:")
    indent {
      appendLine("layout: $layout")
      config.look?.let { appendLine("look: $it") }
      config.theme?.let { appendLine("theme: $it") }
      if (properties.isNotEmpty()) {
        appendLine("$layout:")
        indent {
          for ((key, value) in properties) {
            appendLine("$key: $value")
          }
        }
      }
    }
    appendLine("---")
  }

  private fun IndentedStringBuilder.appendModules() {
    for (module in typedModules) {
      if (module !in links && module.projectPath != thisPath) continue

      appendLine("${module.label}[\"${module.projectPath.cleaned()}\"]")
    }
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
    for (link in links) {
      // TODO https://github.com/jonapoul/modular/issues/121
      val arrow = when (link.style?.lowercase()) {
        "bold" -> "==>"
        "dashed" -> "-.->"
        "dotted" -> "-.->"
        "invis" -> "~~~"
        "solid" -> "-->"
        else -> "-->"
      }
      appendLine("${link.fromPath.label} $arrow ${link.toPath.label}")
    }
  }

  private val TypedModule.label: String
    get() = projectPath.label

  private val String.label: String
    get() = split(":", "-", " ").joinToString("_")

  private fun String.cleaned(): String {
    var string = this
    replacements.forEach { r -> string = string.replace(r.pattern, r.replacement) }
    return string
  }

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
