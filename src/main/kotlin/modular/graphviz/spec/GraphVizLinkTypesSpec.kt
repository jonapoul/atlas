/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.graphviz.spec

import modular.gradle.ModularDsl
import modular.spec.LinkTypesSpec
import org.gradle.internal.impldep.org.intellij.lang.annotations.Language

/**
 * Register link types, to display them differently in the charts based on the relationships between two modules.
 *
 * Bear in mind the configuration strings are expected in regex style, so just entering a raw string will only match
 * that exact string. Use something like `.*?api` if you want to catch `iosMainApi` as well as `api`, for example.
 */
interface GraphVizLinkTypesSpec : LinkTypesSpec {
  @ModularDsl
  operator fun String.invoke(
    style: LinkStyle?,
    color: String? = null,
  ) = invoke(style?.string, color)

  @ModularDsl
  fun add(
    @Language("RegExp") configuration: String,
    style: LinkStyle,
    color: String? = null,
  ) = add(configuration, style.string, color)

  @ModularDsl
  fun add(
    configuration: Regex,
    style: LinkStyle,
    color: String? = null,
  ) = add(configuration, style.string, color)

  @ModularDsl
  fun api(
    style: LinkStyle,
    color: String? = null,
  ) = api(
    style = style.string,
    color = color,
  )

  @ModularDsl
  fun implementation(
    style: LinkStyle,
    color: String? = null,
  ) = implementation(
    style = style.string,
    color = color,
  )
}
