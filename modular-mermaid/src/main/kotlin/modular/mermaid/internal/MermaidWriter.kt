/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.core.internal.IndentedStringBuilder
import modular.core.internal.ModuleLink
import modular.core.internal.TypedModule
import modular.core.internal.buildIndentedString
import modular.core.internal.contains
import modular.core.spec.Replacement
import modular.mermaid.MermaidConfig

internal class MermaidWriter(
  private val typedModules: Set<TypedModule>,
  private val links: Set<ModuleLink>,
  private val replacements: Set<Replacement>,
  private val thisPath: String,
  // TODO https://github.com/jonapoul/modular/issues/123
  @Suppress("unused", "UnusedPrivateProperty") private val groupModules: Boolean,
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
    val layout = config.layout
    val properties = config.layoutProperties.orEmpty()
    appendLine("---")
    appendLine("config:")
    indent {
      layout?.let { appendLine("layout: $it") }
      config.look?.let { appendLine("look: $it") }
      config.theme?.let { appendLine("theme: $it") }
      if (layout != null && properties.isNotEmpty()) {
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
    links.forEachIndexed { i, link ->
      val arrowPrefix = if (config.animateLinks) "link$i@" else ""

      // TODO https://github.com/jonapoul/modular/issues/121
      val arrow = when (link.style?.lowercase()) {
        "bold" -> "==>"
        "dashed" -> "-.->"
        "dotted" -> "-.->"
        "invis" -> "~~~"
        "solid" -> "-->"
        else -> "-->"
      }
      appendLine("${link.fromPath.label} $arrowPrefix$arrow ${link.toPath.label}")
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
