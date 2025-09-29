/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package org.gradle.kotlin.dsl

import modular.core.ModularDsl
import modular.core.internal.StringEnum
import modular.core.spec.LinkTypeSpec
import modular.core.spec.NamedLinkTypeContainer
import org.gradle.api.NamedDomainObjectProvider

@ModularDsl
fun NamedLinkTypeContainer.register(
  configuration: String,
  style: String? = null,
  color: String? = null,
  displayName: String = configuration,
): NamedDomainObjectProvider<LinkTypeSpec> = register(displayName) { spec ->
  spec.configuration.set(configuration)
  spec.style.set(style)
  spec.color.set(color)
}

@JvmOverloads
@ModularDsl
fun NamedLinkTypeContainer.register(
  configuration: String,
  style: StringEnum,
  color: String? = null,
  displayName: String = configuration,
): NamedDomainObjectProvider<LinkTypeSpec> = register(configuration, style.string, color, displayName)

@JvmOverloads
@ModularDsl
fun NamedLinkTypeContainer.api(
  style: String? = null,
  color: String? = null,
  displayName: String = "api",
): NamedDomainObjectProvider<LinkTypeSpec> = register(configuration = ".*?api", style, color, displayName)

@JvmOverloads
@ModularDsl
fun NamedLinkTypeContainer.api(
  style: StringEnum,
  color: String? = null,
  displayName: String = "api",
): NamedDomainObjectProvider<LinkTypeSpec> = api(style.string, color, displayName)

@JvmOverloads
@ModularDsl
fun NamedLinkTypeContainer.implementation(
  style: String? = null,
  color: String? = null,
  displayName: String = "implementation",
): NamedDomainObjectProvider<LinkTypeSpec> = register(configuration = ".*?implementation", style, color, displayName)

@JvmOverloads
@ModularDsl
fun NamedLinkTypeContainer.implementation(
  style: StringEnum,
  color: String? = null,
  displayName: String = "implementation",
): NamedDomainObjectProvider<LinkTypeSpec> = implementation(style.string, color, displayName)
