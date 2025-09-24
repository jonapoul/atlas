/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package org.gradle.kotlin.dsl

import modular.core.spec.LinkTypeSpec
import modular.core.spec.Style
import modular.gradle.ModularDsl
import org.gradle.api.NamedDomainObjectContainer

@JvmOverloads
@ModularDsl
fun NamedDomainObjectContainer<LinkTypeSpec>.register(
  configuration: String,
  style: String? = null,
  color: String? = null,
  displayName: String = configuration,
) = register(displayName) { spec ->
  spec.configuration.set(configuration)
  spec.style.set(style)
  spec.color.set(color)
}

@JvmOverloads
@ModularDsl
fun NamedDomainObjectContainer<LinkTypeSpec>.register(
  configuration: String,
  style: Style,
  color: String? = null,
  displayName: String = configuration,
) = register(configuration, style.string, color, displayName)

@JvmOverloads
@ModularDsl
context(container: NamedDomainObjectContainer<LinkTypeSpec>)
operator fun String.invoke(
  style: String? = null,
  color: String? = null,
  displayName: String = this,
) = container.register(configuration = this, style, color, displayName)

@JvmOverloads
@ModularDsl
context(container: NamedDomainObjectContainer<LinkTypeSpec>)
operator fun String.invoke(
  style: Style?,
  color: String? = null,
  displayName: String = this,
) = container.register(configuration = this, style?.string, color, displayName)

@JvmOverloads
@ModularDsl
fun NamedDomainObjectContainer<LinkTypeSpec>.api(
  style: String? = null,
  color: String? = null,
  displayName: String = "api",
) = register(configuration = ".*?api", style, color, displayName)

@JvmOverloads
@ModularDsl
fun NamedDomainObjectContainer<LinkTypeSpec>.api(
  style: Style,
  color: String? = null,
  displayName: String = "api",
) = api(style.string, color, displayName)

@JvmOverloads
@ModularDsl
fun NamedDomainObjectContainer<LinkTypeSpec>.implementation(
  style: String? = null,
  color: String? = null,
  displayName: String = "implementation",
) = register(configuration = ".*?implementation", style, color, displayName)

@JvmOverloads
@ModularDsl
fun NamedDomainObjectContainer<LinkTypeSpec>.implementation(
  style: Style,
  color: String? = null,
  displayName: String = "implementation",
) = implementation(style.string, color, displayName)
