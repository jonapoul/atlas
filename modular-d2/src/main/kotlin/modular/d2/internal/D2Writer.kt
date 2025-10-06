/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("TooManyFunctions", "LongParameterList")

package modular.d2.internal

import modular.core.InternalModularApi
import modular.core.Replacement
import modular.core.internal.ChartWriter
import modular.core.internal.IndentedStringBuilder
import modular.core.internal.ModuleLink
import modular.core.internal.Subgraph
import modular.core.internal.TypedModule
import modular.core.internal.buildIndentedString

@InternalModularApi
class D2Writer(
  override val typedModules: Set<TypedModule>,
  override val links: Set<ModuleLink>,
  override val replacements: Set<Replacement>,
  override val thisPath: String,
  override val groupModules: Boolean,
  private val globalRelativePath: String,
) : ChartWriter() {
  private var subgraphNestingLevel = 0

  override fun invoke(): String = buildIndentedString {
    appendImports()
    appendModules()
    appendLine("${thisPath.fullKey()}.style.stroke-width: 8")
    appendLinks()
  }

  private fun IndentedStringBuilder.appendImports() {
    appendLine("...@$globalRelativePath")
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
