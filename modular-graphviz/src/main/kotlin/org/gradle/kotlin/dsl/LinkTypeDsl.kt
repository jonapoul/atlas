/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package org.gradle.kotlin.dsl

import modular.core.LinkTypeSpec
import modular.graphviz.GraphvizNamedLinkTypeContainer
import modular.graphviz.LinkStyle
import org.gradle.api.NamedDomainObjectProvider

@JvmOverloads
public fun GraphvizNamedLinkTypeContainer.register(
  configuration: String,
  style: LinkStyle? = null,
  color: String? = null,
  displayName: String = configuration,
): NamedDomainObjectProvider<LinkTypeSpec> = register(displayName) { spec ->
  spec.configuration.set(configuration)
  spec.style.set(style?.string)
  spec.color.set(color)
}

@JvmOverloads
public fun GraphvizNamedLinkTypeContainer.api(
  style: LinkStyle? = null,
  color: String? = null,
  displayName: String = "api",
): NamedDomainObjectProvider<LinkTypeSpec> = register(
  configuration = ".*?api",
  style = style,
  color = color,
  displayName = displayName,
)

@JvmOverloads
public fun GraphvizNamedLinkTypeContainer.implementation(
  style: LinkStyle? = null,
  color: String? = null,
  displayName: String = "implementation",
): NamedDomainObjectProvider<LinkTypeSpec> = register(
  configuration = ".*?implementation",
  style = style,
  color = color,
  displayName = displayName,
)
