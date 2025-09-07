/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused")

package org.gradle.kotlin.dsl

import modular.gradle.ModuleType
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider

/**
 * See https://coolors.co/09ad35-55ff55-ff8800-ca66ff-9d8df1 for the default colors
 */

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.androidApp(
  name: String = "Android App",
  color: String = "#09AD35", // darker green
): NamedDomainObjectProvider<ModuleType> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "com.android.application",
)

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.androidLibrary(
  name: String = "Android Library",
  color: String = "#55FF55", // light green
): NamedDomainObjectProvider<ModuleType> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "com.android.library",
)

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.java(
  name: String = "Java",
  color: String = "#FF8800", // orange
): NamedDomainObjectProvider<ModuleType> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "java",
)

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.kotlinJvm(
  name: String = "Kotlin JVM",
  color: String = "#CA66FF", // lavender
): NamedDomainObjectProvider<ModuleType> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.jvm",
)

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.kotlinMultiplatform(
  name: String = "Kotlin Multiplatform",
  color: String = "#9D8DF1", // lilac
): NamedDomainObjectProvider<ModuleType> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.multiplatform",
)

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.other(
  name: String = "Other",
  color: String = "#808080", // grey
): NamedDomainObjectProvider<ModuleType> = registerByPathMatches(
  name = name,
  color = color,
  pathMatches = ".*?".toRegex(),
)

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.builtIns() {
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
fun NamedDomainObjectContainer<ModuleType>.registerByPluginId(
  name: String,
  color: String,
  pluginId: String,
): NamedDomainObjectProvider<ModuleType> = register(name) { type ->
  type.color.set(color)
  type.hasPluginId.set(pluginId)
}

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.registerByPathMatches(
  name: String,
  color: String,
  pathMatches: Regex,
): NamedDomainObjectProvider<ModuleType> = register(name) { type ->
  type.color.set(color)
  type.pathMatches.set(pathMatches)
}

@ModularDsl
fun NamedDomainObjectContainer<ModuleType>.registerByPathContains(
  name: String,
  color: String,
  pathContains: String,
): NamedDomainObjectProvider<ModuleType> = register(name) { type ->
  type.color.set(color)
  type.pathContains.set(pathContains)
}

@DslMarker
annotation class ModularDsl
