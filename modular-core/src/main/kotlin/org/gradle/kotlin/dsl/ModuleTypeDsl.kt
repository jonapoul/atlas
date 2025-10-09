/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package org.gradle.kotlin.dsl

import modular.core.ModuleTypeSpec
import modular.core.NamedModuleTypeContainer
import org.gradle.api.NamedDomainObjectProvider

@JvmOverloads
fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.androidApp(
  name: String = "Android App",
  color: String = "limegreen",
  extraConfig: () -> Unit = {},
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "com.android.application",
)

@JvmOverloads
fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.androidLibrary(
  name: String = "Android Library",
  color: String = "lightgreen",
  extraConfig: () -> Unit = {},
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "com.android.library",
)

@JvmOverloads
fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.java(
  name: String = "Java",
  color: String = "orange",
  extraConfig: () -> Unit = {},
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "java",
)

@JvmOverloads
fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.kotlinJvm(
  name: String = "Kotlin JVM",
  color: String = "mediumorchid",
  extraConfig: () -> Unit = {},
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.jvm",
)

@JvmOverloads
fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.kotlinMultiplatform(
  name: String = "Kotlin Multiplatform",
  color: String = "mediumslateblue",
  extraConfig: () -> Unit = {},
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.multiplatform",
)

@JvmOverloads
fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.other(
  name: String = "Other",
  color: String = "gainsboro",
  extraConfig: () -> Unit = {},
): NamedDomainObjectProvider<T> = registerByPathMatches(
  name = name,
  color = color,
  pathMatches = ".*?".toRegex(), // match anything and everything - should always have this declared last
)

fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.builtIns() {
  // Highest priority ↓↓
  androidApp()
  kotlinMultiplatform()
  androidLibrary()
  kotlinJvm()
  java()
  other()
  // Lowest priority ↑↑
}
