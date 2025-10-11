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
public fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.androidApp(
  name: String = "Android App",
  color: String? = "limegreen",
  extraConfig: T.() -> Unit = {},
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "com.android.application",
  extraConfig = extraConfig,
)

@JvmOverloads
public fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.androidLibrary(
  name: String = "Android Library",
  color: String? = "lightgreen",
  extraConfig: T.() -> Unit = {},
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "com.android.library",
  extraConfig = extraConfig,
)

@JvmOverloads
public fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.java(
  name: String = "Java",
  color: String? = "orange",
  extraConfig: T.() -> Unit = {},
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "java",
  extraConfig = extraConfig,
)

@JvmOverloads
public fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.kotlinJvm(
  name: String = "Kotlin JVM",
  color: String? = "mediumorchid",
  extraConfig: T.() -> Unit = {},
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.jvm",
  extraConfig = extraConfig,
)

@JvmOverloads
public fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.kotlinMultiplatform(
  name: String = "Kotlin Multiplatform",
  color: String? = "mediumslateblue",
  extraConfig: T.() -> Unit = {},
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.multiplatform",
  extraConfig = extraConfig,
)

@JvmOverloads
public fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.other(
  name: String = "Other",
  color: String? = "gainsboro",
  extraConfig: T.() -> Unit = {},
): NamedDomainObjectProvider<T> = registerByPathMatches(
  name = name,
  color = color,
  pathMatches = ".*?", // match anything and everything - should always have this declared last
  extraConfig = extraConfig,
)

public fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.useDefaults() {
  // Highest priority ↓↓
  androidApp()
  kotlinMultiplatform()
  androidLibrary()
  kotlinJvm()
  java()
  other()
  // Lowest priority ↑↑
}
