/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package org.gradle.kotlin.dsl

import modular.core.LinkStyle
import modular.core.LinkTypeSpec
import modular.core.NamedLinkTypeContainer
import org.gradle.api.NamedDomainObjectProvider

@JvmOverloads
fun NamedLinkTypeContainer.register(
  configuration: String,
  style: LinkStyle? = null,
  color: String? = null,
  displayName: String = configuration,
): NamedDomainObjectProvider<LinkTypeSpec> = register(displayName) { spec ->
  spec.configuration.set(configuration)
  spec.style.set(style)
  spec.color.set(color)
}

@JvmOverloads
fun NamedLinkTypeContainer.api(
  style: LinkStyle? = null,
  color: String? = null,
  displayName: String = "api",
): NamedDomainObjectProvider<LinkTypeSpec> = register(configuration = ".*?api", style, color, displayName)

@JvmOverloads
fun NamedLinkTypeContainer.implementation(
  style: LinkStyle? = null,
  color: String? = null,
  displayName: String = "implementation",
): NamedDomainObjectProvider<LinkTypeSpec> = register(configuration = ".*?implementation", style, color, displayName)
