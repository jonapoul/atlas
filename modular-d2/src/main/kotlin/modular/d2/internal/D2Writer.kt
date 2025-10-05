/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("TooManyFunctions")

package modular.d2.internal

import modular.core.InternalModularApi
import modular.core.LinkStyle
import modular.core.Replacement
import modular.core.internal.ChartWriter
import modular.core.internal.IndentedStringBuilder
import modular.core.internal.ModuleLink
import modular.core.internal.Subgraph
import modular.core.internal.TypedModule
import modular.core.internal.buildIndentedString
import modular.d2.ArrowType
import modular.d2.D2Config
import modular.d2.Position

@InternalModularApi
data class D2Writer(
  override val typedModules: Set<TypedModule>,
  override val links: Set<ModuleLink>,
  override val replacements: Set<Replacement>,
  override val thisPath: String,
  override val groupModules: Boolean,
  private val config: D2Config,
) : ChartWriter() {
  private var subgraphNestingLevel = 0

  override fun invoke(): String = buildIndentedString(size = 2) {
    appendVars()
    appendStyles()
    appendModules()
    appendLinks()
    appendGlobs()
  }

  private fun IndentedStringBuilder.appendVars() = with(config) {
    val attrs = mapOf<String, Any?>(
      "theme-id" to theme?.value,
      "dark-theme-id" to darkTheme?.value,
      "layout-engine" to layoutEngine?.string,
      "pad" to pad,
      "sketch" to sketch,
      "center" to center,
    )
    if (attrs.count { it.value != null } == 0) {
      return@with
    }

    appendLine("vars: {")
    indent {
      appendLine("d2-config: {")
      indent {
        attrs.sortedByKeys().forEach { (key, value) ->
          if (value != null) {
            appendLine("$key: $value")
          }
        }
      }
      appendLine("}")
    }
    appendLine("}")
  }

  private fun IndentedStringBuilder.appendStyles() = with(config) {
    direction?.let { appendLine("direction: $it") }

    if (rootStyle.isNullOrEmpty()) return@with
    appendLine("style: {")
    indent {
      rootStyle.forEach { (key, value) ->
        appendLine("$key: \"$value\"")
      }
    }
    appendLine("}")
  }

  override fun IndentedStringBuilder.appendSubgraphHeader(graph: Subgraph) {
    val key = graph.path[subgraphNestingLevel].cleaned()
    appendLine("$key: :${graph.name} {")
    groupLabelSpecifier()?.let { pos -> indent { appendLine("label.near: $pos") } }
    subgraphNestingLevel++
  }

  override fun IndentedStringBuilder.appendSubgraphFooter() {
    appendLine("}")
    subgraphNestingLevel--
  }

  override fun IndentedStringBuilder.appendModule(module: TypedModule) {
    val key = module.projectPath.localKey()
    val name = if (groupModules) ":$key" else module.projectPath
    appendLine("$key: $name")
  }

  private fun IndentedStringBuilder.appendLinks() {
    links
      .map { link -> link.copy(fromPath = link.fromPath.cleaned(), toPath = link.toPath.cleaned()) }
      .sortedWith(compareBy({ it.fromPath.fullKey() }, { it.toPath.fullKey() }))
      .forEach { (fromPath, toPath, _, style, color) ->
        val linkString = "${fromPath.fullKey()} -> ${toPath.fullKey()}"
        val attrs = linkAttributes(style, color)
        if (attrs.isNotEmpty()) {
          appendLine("$linkString: {")
          indent {
            attrs.sortedByKeys().forEach { (key, value) ->
              appendLine("$key: \"$value\"")
            }
          }
          appendLine("}")
        } else {
          appendLine(linkString)
        }
      }
  }

  private fun IndentedStringBuilder.appendGlobs() = config.globalProps?.let { props ->
    props.sortedByKeys().forEach { (key, value) ->
      appendLine("$key: $value")
    }
  }

  private fun linkAttributes(style: LinkStyle?, color: String?): Map<String, String> {
    val attrs = mutableMapOf<String, String>()
    color?.let { attrs["style.stroke"] = it }

    when (style) {
      LinkStyle.Dashed -> attrs["style.stroke-dash"] = "4"
      LinkStyle.Dotted -> attrs["style.stroke-dash"] = "2"
      LinkStyle.Solid -> Unit
      LinkStyle.Invis -> attrs["style.opacity"] = "0"
      LinkStyle.Bold -> attrs["style.stroke-width"] = "3" // default 2
      null -> Unit
    }

    if (config.animateLinks == true && style in ANIMATABLE_LINK_TYPES) {
      attrs["style.animated"] = "true"
    }

    return attrs
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

  private fun <T> Map<String, T>.sortedByKeys(): List<Pair<String, T>> = toList().sortedBy { (k, _) -> k }

  private fun groupLabelSpecifier(): String? {
    val position = config.groupLabelPosition ?: return null // e.g. top-right
    val location = config.groupLabelLocation // e.g. outside
    return if (location == null) {
      position.toString()
    } else {
      // need to swap the order to "left-center" if we have a location specifier prefix
      val rejiggedPosition = if (position in setOf(Position.CenterLeft, Position.CenterRight)) {
        val split = position.toString().split("-")
        "${split[1]}-${split[0]}"
      } else {
        position.toString()
      }
      "$location-$rejiggedPosition"
    }
  }

  private companion object {
    val DISALLOWED_SUBSTRINGS = setOf("{", "}", "->", "--", "<-", "<->", "|", "#", "\"", "'")
    val FILLABLE_ARROW_TYPES = setOf(ArrowType.Triangle, ArrowType.Diamond, ArrowType.Circle, ArrowType.Box)
    val ANIMATABLE_LINK_TYPES = setOf(LinkStyle.Dashed, LinkStyle.Dotted)
  }
}
