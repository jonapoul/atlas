/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package org.gradle.kotlin.dsl

import atlas.core.ModuleTypeSpec
import atlas.core.NamedModuleTypeContainer
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectProvider

/**
 * Adds an "Android App" module type, with a lime green color.
 */
@JvmOverloads
public fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.androidApp(
  name: String = "Android App",
  color: String? = "limegreen",
  action: Action<T>? = null,
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "com.android.application",
  action = action,
)

/**
 * Adds an "Android Library" module type, with a lighter green color.
 */
@JvmOverloads
public fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.androidLibrary(
  name: String = "Android Library",
  color: String? = "lightgreen",
  action: Action<T>? = null,
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "com.android.library",
  action = action,
)

/**
 * Adds a "Java" module type, with an orange color.
 */
@JvmOverloads
public fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.java(
  name: String = "Java",
  color: String? = "orange",
  action: Action<T>? = null,
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "java",
  action = action,
)

/**
 * Adds a "Kotlin JVM" module type, with a darkish pink color.
 */
@JvmOverloads
public fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.kotlinJvm(
  name: String = "Kotlin JVM",
  color: String? = "mediumorchid",
  action: Action<T>? = null,
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.jvm",
  action = action,
)

/**
 * Adds a "Kotlin Multiplatform" module type, with a purplish blue color.
 */
@JvmOverloads
public fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.kotlinMultiplatform(
  name: String = "Kotlin Multiplatform",
  color: String? = "mediumslateblue",
  action: Action<T>? = null,
): NamedDomainObjectProvider<T> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.multiplatform",
  action = action,
)

/**
 * Adds an "Other" module type, with a grey color. This will match anything and everything, so make sure to declare this
 * last in the moduleTypes block from your build file.
 */
@JvmOverloads
public fun <T : ModuleTypeSpec> NamedModuleTypeContainer<T>.other(
  name: String = "Other",
  color: String? = "gainsboro",
  action: Action<T>? = null,
): NamedDomainObjectProvider<T> = registerByPathMatches(
  name = name,
  color = color,
  pathMatches = ".*?", // match anything and everything - should always have this declared last
  action = action,
)

/**
 * Adds a set of standard module types to your config.
 */
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
