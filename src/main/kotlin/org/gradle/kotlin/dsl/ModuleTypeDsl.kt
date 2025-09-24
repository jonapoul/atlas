/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package org.gradle.kotlin.dsl

import modular.core.spec.ModuleTypeSpec
import modular.core.spec.NamedModuleTypeContainer
import modular.gradle.ModularDsl
import org.gradle.api.NamedDomainObjectProvider

@ModularDsl
@JvmOverloads
fun NamedModuleTypeContainer.androidApp(
  name: String = "Android App",
  color: String = "limegreen",
): NamedDomainObjectProvider<ModuleTypeSpec> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "com.android.application",
)

@ModularDsl
@JvmOverloads
fun NamedModuleTypeContainer.androidLibrary(
  name: String = "Android Library",
  color: String = "lightgreen",
): NamedDomainObjectProvider<ModuleTypeSpec> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "com.android.library",
)

@ModularDsl
@JvmOverloads
fun NamedModuleTypeContainer.java(
  name: String = "Java",
  color: String = "orange",
): NamedDomainObjectProvider<ModuleTypeSpec> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "java",
)

@ModularDsl
@JvmOverloads
fun NamedModuleTypeContainer.kotlinJvm(
  name: String = "Kotlin JVM",
  color: String = "mediumorchid",
): NamedDomainObjectProvider<ModuleTypeSpec> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.jvm",
)

@ModularDsl
@JvmOverloads
fun NamedModuleTypeContainer.kotlinMultiplatform(
  name: String = "Kotlin Multiplatform",
  color: String = "mediumslateblue",
): NamedDomainObjectProvider<ModuleTypeSpec> = registerByPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.multiplatform",
)

@ModularDsl
@JvmOverloads
fun NamedModuleTypeContainer.other(
  name: String = "Other",
  color: String = "gainsboro",
): NamedDomainObjectProvider<ModuleTypeSpec> = registerByPathMatches(
  name = name,
  color = color,
  pathMatches = ".*?".toRegex(), // match anything and everything - should always have this declared last
)

@ModularDsl
fun NamedModuleTypeContainer.builtIns() {
  // Highest priority ↓↓
  androidApp()
  kotlinMultiplatform()
  androidLibrary()
  kotlinJvm()
  java()
  other()
  // Lowest priority ↑↑
}
