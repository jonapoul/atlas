/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("NestedBlockDepth", "LongParameterList")

package modular.d2.internal

import modular.core.InternalModularApi
import modular.core.LinkType
import modular.core.ModuleType
import modular.core.internal.IndentedStringBuilder
import modular.core.internal.buildIndentedString
import modular.core.internal.moduleType
import modular.core.internal.orderedLinkTypes
import modular.core.internal.orderedModuleTypes
import modular.core.internal.parseEnum
import modular.d2.Direction
import modular.d2.LayoutEngine
import modular.d2.LinkStyle
import modular.d2.Location
import modular.d2.Position
import modular.d2.Position.CenterLeft
import modular.d2.Position.CenterRight
import modular.d2.Theme
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

@InternalModularApi
fun writeD2Classes(config: D2ClassesConfig): String = buildIndentedString {
  appendStyles(config)
  appendVars(config)

  appendLine("classes: {")
  indent {
    for (type in config.moduleTypes) appendClass(type)
    for (type in config.linkTypes) appendLink(config, type)
    appendContainer(config)
    appendHidden()
    appendThisProject()
  }
  appendLine("}")

  appendGlobs(config)
}

@KSerializable
class D2ClassesConfig(
  val animateLinks: Boolean? = null,
  val center: Boolean? = null,
  val darkTheme: Theme? = null,
  val direction: Direction? = null,
  val globalProps: Map<String, String>? = null,
  val layoutEngine: LayoutEngine? = null,
  val linkTypes: List<LinkType> = emptyList(),
  val location: Location? = null,
  val moduleTypes: List<ModuleType> = emptyList(),
  val pad: Int? = null,
  val position: Position? = null,
  val rootStyle: Map<String, String> = emptyMap(),
  val sketch: Boolean? = null,
  val theme: Theme? = null,
) : JSerializable

internal fun D2ModularExtensionImpl.toConfig() = D2ClassesConfig(
  animateLinks = d2.animateLinks.orNull,
  center = d2.center.orNull,
  darkTheme = d2.themeDark.orNull,
  direction = d2.direction.orNull,
  globalProps = d2.globalProps.properties.orNull,
  layoutEngine = d2.layoutEngine.orNull,
  linkTypes = orderedLinkTypes(),
  location = d2.groupLabelLocation.orNull,
  moduleTypes = orderedModuleTypes().map(::moduleType),
  pad = d2.pad.orNull,
  position = d2.groupLabelPosition.orNull,
  rootStyle = d2.rootStyle.properties.getOrElse(mutableMapOf()),
  sketch = d2.sketch.orNull,
  theme = d2.theme.orNull,
)

internal const val CONTAINER_CLASS = "container"
internal const val HIDDEN_CLASS = "hidden"
internal const val THIS_PROJECT_CLASS = "thisProject"

internal val LinkType.classId get() = "link-$key"
internal val ModuleType.classId get() = "module-$key"

internal val LinkType.key: String get() = configuration.key
internal val ModuleType.key: String get() = name.key

private val String.key
  get() = this.filter { it.isLetter() || it.isDigit() }

private fun IndentedStringBuilder.appendClass(type: ModuleType) = with(type) {
  appendLine("$classId {")
  indent {
    appendLine("style.fill: \"$color\"")
  }
  appendLine("}")
}

private fun IndentedStringBuilder.appendLink(config: D2ClassesConfig, type: LinkType) {
  appendLine("${type.classId} {")
  indent {
    for ((key, value) in linkAttributes(config, type)) {
      appendLine("$key: \"$value\"")
    }
  }
  appendLine("}")
}

private fun linkAttributes(config: D2ClassesConfig, link: LinkType): List<Pair<String, String>> {
  val attrs = mutableMapOf<String, String>()
  link.color?.let { attrs["style.stroke"] = it }

  val style = link.style?.let { parseEnum<LinkStyle>(it) }
  when (style) {
    LinkStyle.Dashed -> attrs["style.stroke-dash"] = "4"
    LinkStyle.Dotted -> attrs["style.stroke-dash"] = "2"
    LinkStyle.Basic -> Unit
    LinkStyle.Invisible -> attrs["style.opacity"] = "0"
    LinkStyle.Bold -> attrs["style.stroke-width"] = "3" // default 2
    null -> Unit
  }

  if (config.animateLinks == true && style in ANIMATABLE_LINK_TYPES) {
    attrs["style.animated"] = "true"
  }

  return attrs.sortedByKeys()
}

private fun IndentedStringBuilder.appendContainer(config: D2ClassesConfig) {
  appendLine("$CONTAINER_CLASS {")
  indent { appendGroupLabelSpecifier(config) }
  appendLine("}")
}

private fun IndentedStringBuilder.appendHidden() {
  appendLine("$HIDDEN_CLASS {")
  indent { appendLine("style.opacity: 0") }
  appendLine("}")
}

private fun IndentedStringBuilder.appendThisProject() {
  appendLine("$THIS_PROJECT_CLASS {")
  indent { appendLine("style.stroke-width: 8") }
  appendLine("}")
}

private fun IndentedStringBuilder.appendGroupLabelSpecifier(config: D2ClassesConfig) = with(config) {
  position ?: return@with // e.g. top-right
  if (location == null) {
    appendLine("label.near: $position")
  } else {
    // need to swap the order to "left-center" if we have a location specifier prefix
    val rejiggedPosition = if (position in setOf(CenterLeft, CenterRight)) {
      position.toString().split("-").let { str -> "${str[1]}-${str[0]}" }
    } else {
      position.toString()
    }
    appendLine("label.near: $location-$rejiggedPosition")
  }
}

private fun IndentedStringBuilder.appendStyles(config: D2ClassesConfig) = with(config) {
  direction?.let { appendLine("direction: $it") }

  if (rootStyle.isEmpty()) return@with
  appendLine("style: {")
  indent {
    rootStyle.forEach { (key, value) ->
      appendLine("$key: \"$value\"")
    }
  }
  appendLine("}")
}

private fun IndentedStringBuilder.appendVars(config: D2ClassesConfig) = with(config) {
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

private fun IndentedStringBuilder.appendGlobs(config: D2ClassesConfig) = config.globalProps?.let { props ->
  props.sortedByKeys().forEach { (key, value) ->
    appendLine("$key: $value")
  }
}

private val ANIMATABLE_LINK_TYPES = setOf(LinkStyle.Dashed, LinkStyle.Dotted)
