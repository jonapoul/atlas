/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.graphviz.spec

import modular.gradle.ExperimentalModularApi
import modular.gradle.ModularDsl
import modular.spec.ModuleLinksSpec
import kotlin.text.RegexOption.IGNORE_CASE

interface GraphVizModuleLinksSpec : ModuleLinksSpec {
  @ExperimentalModularApi
  @ModularDsl
  operator fun String.invoke(
    style: LinkStyle?,
    color: String? = null,
  ) = invoke(style?.string, color)

  @ModularDsl
  fun add(
    configuration: String,
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
  override fun api(color: String?) = add(
    configuration = ".*?api".toRegex(IGNORE_CASE),
    style = LinkStyle.Solid,
    color = color,
  )

  @ModularDsl
  override fun implementation(color: String?) = add(
    configuration = ".*?implementation".toRegex(IGNORE_CASE),
    style = LinkStyle.Dotted,
    color = color,
  )
}
