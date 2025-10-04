/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.internal

import modular.core.InternalModularApi
import modular.core.internal.ChartWriter
import modular.core.internal.IndentedStringBuilder
import modular.core.internal.ModuleLink
import modular.core.internal.TypedModule
import modular.core.internal.buildIndentedString
import modular.core.spec.Replacement
import modular.d2.D2Config

@InternalModularApi
data class D2Writer(
  override val typedModules: Set<TypedModule>,
  override val links: Set<ModuleLink>,
  override val replacements: Set<Replacement>,
  override val thisPath: String,
  override val groupModules: Boolean,
  private val config: D2Config,
) : ChartWriter() {
  operator fun invoke(): String = buildIndentedString(size = 2) {
    appendStyles()
    appendModules()
    appendLinks()
  }

  private fun IndentedStringBuilder.appendStyles() = with(config) {
    direction?.let { appendLine("direction: $it") }

    if (style.isNullOrEmpty()) return@with
    appendLine("style: {")
    indent {
      style.forEach { (key, value) ->
        appendLine("$key: \"$value\"")
      }
    }
    appendLine("}")
  }

  override fun IndentedStringBuilder.appendSubgraphHeader(cleanedModuleName: String, displayName: String) {
    // TODO https://github.com/jonapoul/modular/issues/180
    appendLine("$cleanedModuleName: $displayName {")
  }

  override fun IndentedStringBuilder.appendSubgraphFooter() {
    appendLine("}")
  }

  override fun IndentedStringBuilder.appendModule(module: TypedModule) {
    appendLine("${module.projectPath.localKey()}: ${module.projectPath.cleaned()}")
  }

  private fun IndentedStringBuilder.appendLinks(): IndentedStringBuilder {
    links
      .map { link -> link.copy(fromPath = link.fromPath.cleaned(), toPath = link.toPath.cleaned()) }
      .sortedWith(compareBy({ it.fromPath.fullKey() }, { it.toPath.fullKey() }))
      .forEach { (fromPath, toPath, _, _, _) ->
        // TODO https://github.com/jonapoul/modular/issues/181
        appendLine("${fromPath.fullKey()} -> ${toPath.fullKey()}")
      }
    return this
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
    // "path.to.my.module" -> "module"
    fullKey().split(".").last()
  } else {
    fullKey()
  }

  private companion object {
    val DISALLOWED_SUBSTRINGS = setOf("{", "}", "->", "--", "<-", "<->", "|", "#", "\"", "'")
  }
}
