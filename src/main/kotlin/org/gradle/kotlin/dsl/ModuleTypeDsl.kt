/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package org.gradle.kotlin.dsl

import modular.core.spec.ModuleTypeSpec
import modular.gradle.ModularDsl
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

/**
 * See https://coolors.co/09ad35-55ff55-ff8800-ca66ff-9d8df1 for the default colors
 */

@ModularDsl
@JvmOverloads
fun NamedDomainObjectContainer<ModuleTypeSpec>.androidApp(
  name: String = "Android App",
  color: String = "#09AD35", // darker green
): NamedDomainObjectProvider<ModuleTypeSpec> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "com.android.application",
)

@ModularDsl
@JvmOverloads
fun NamedDomainObjectContainer<ModuleTypeSpec>.androidLibrary(
  name: String = "Android Library",
  color: String = "#55FF55", // light green
): NamedDomainObjectProvider<ModuleTypeSpec> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "com.android.library",
)

@ModularDsl
@JvmOverloads
fun NamedDomainObjectContainer<ModuleTypeSpec>.java(
  name: String = "Java",
  color: String = "#FF8800", // orange
): NamedDomainObjectProvider<ModuleTypeSpec> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "java",
)

@ModularDsl
@JvmOverloads
fun NamedDomainObjectContainer<ModuleTypeSpec>.kotlinJvm(
  name: String = "Kotlin JVM",
  color: String = "#CA66FF", // lavender
): NamedDomainObjectProvider<ModuleTypeSpec> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.jvm",
)

@ModularDsl
@JvmOverloads
fun NamedDomainObjectContainer<ModuleTypeSpec>.kotlinMultiplatform(
  name: String = "Kotlin Multiplatform",
  color: String = "#9D8DF1", // lilac
): NamedDomainObjectProvider<ModuleTypeSpec> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.multiplatform",
)

@ModularDsl
@JvmOverloads
fun NamedDomainObjectContainer<ModuleTypeSpec>.other(
  name: String = "Other",
  color: String = "#808080", // grey
): NamedDomainObjectProvider<ModuleTypeSpec> = registerByPathMatches(
  name = name,
  color = color,
  pathMatches = ".*?".toRegex(),
)

@ModularDsl
fun NamedDomainObjectContainer<ModuleTypeSpec>.builtIns() {
  // Highest priority ↓↓
  androidApp()
  kotlinMultiplatform()
  androidLibrary()
  kotlinJvm()
  java()
  other()
  // Lowest priority ↑↑
}

@ModularDsl
fun NamedDomainObjectContainer<ModuleTypeSpec>.registerByPluginId(
  name: String,
  color: String,
  pluginId: String,
): NamedDomainObjectProvider<ModuleTypeSpec> = register(name) { type ->
  type.color.convention(color)
  type.hasPluginId.convention(pluginId)
}

@ModularDsl
fun NamedDomainObjectContainer<ModuleTypeSpec>.registerByPathMatches(
  name: String,
  color: String,
  pathMatches: Regex,
): NamedDomainObjectProvider<ModuleTypeSpec> = register(name) { type ->
  type.color.convention(color)
  type.pathMatches.convention(pathMatches)
}

@ModularDsl
fun NamedDomainObjectContainer<ModuleTypeSpec>.registerByPathContains(
  name: String,
  color: String,
  pathContains: String,
): NamedDomainObjectProvider<ModuleTypeSpec> = register(name) { type ->
  type.color.convention(color)
  type.pathContains.convention(pathContains)
}
