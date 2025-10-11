/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.core

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.provider.Property

/**
 * Represents a category of module that you can use to match against those in your project. You can use some of the
 * built-in example types like below:
 *
 * ```kotlin
 * modular {
 *   moduleTypes {
 *     androidApp()
 *     androidLibrary()
 *     java()
 *     kotlinJvm()
 *     kotlinMultiplatform()
 *     other()
 *
 *     // or use useDefaults() to include all of the above
 *   }
 * }
 * ```
 *
 * or create custom types like:
 *
 * ```kotlin
 * modular {
 *   moduleTypes {
 *     // reference some built-in types
 *     androidApp()
 *     java {
 *       // custom overrides
 *       color = "black"
 *     }
 *
 *     // plus some manually-defined ones
 *     registerByPluginId(name = "UI", color = "#ABC123", pluginId = "org.jetbrains.kotlin.plugin.compose")
 *     registerByPathMatches(name = "Data", color = "#ABCDEF", pathMatches = ".*data$".toRegex())
 *     registerByPathContains(name = "Domain", color = "#123ABC", pathContains = "domain")
 *   }
 * }
 * ```
 *
 * Remember that priority is given in descending order, so in the example above the UI module type will be checked
 * before the data or domain types.
 *
 * Exactly one of [ModuleTypeSpec.pathContains], [ModuleTypeSpec.pathMatches] or [ModuleTypeSpec.hasPluginId] must be
 * set. If not, Gradle will:
 * - warn you during IDE sync, or
 * - fail when running any tasks which reference them.
 */
@ModularDsl
interface NamedModuleTypeContainer<T : ModuleTypeSpec> : NamedDomainObjectContainer<T> {
  fun registerByPluginId(
    name: String,
    color: String,
    pluginId: String,
    extraConfig: T.() -> Unit = {},
  ): NamedDomainObjectProvider<T> = register(name) { type ->
    type.color.convention(color)
    type.hasPluginId.convention(pluginId)
    type.extraConfig()
  }

  fun registerByPathMatches(
    name: String,
    color: String,
    pathMatches: Regex,
    extraConfig: T.() -> Unit = {},
  ): NamedDomainObjectProvider<T> = register(name) { type ->
    type.color.convention(color)
    type.pathMatches.convention(pathMatches.toString())
    type.extraConfig()
  }

  fun registerByPathContains(
    name: String,
    color: String,
    pathContains: String,
    extraConfig: T.() -> Unit = {},
  ): NamedDomainObjectProvider<T> = register(name) { type ->
    type.color.convention(color)
    type.pathContains.convention(pathContains)
    type.extraConfig()
  }
}

interface ModuleTypeSpec : PropertiesSpec {
  /**
   * Required - this will be shown on your generated legend files.
   */
  val name: String

  /**
   * Optional - defaults to #FFFFFF (white). Must be prefixed with a # followed by a 6-character hexadecimal string.
   */
  val color: Property<String>

  /**
   * Checks against the path string of your module, e.g. ":path:to:my:module". This is case-sensitive.
   */
  val pathContains: Property<String>

  /**
   * Similar to [pathContains] but more flexible with [Regex] pattern checking instead of straight string comparison.
   */
  val pathMatches: Property<String>

  /**
   * Checks whether the given plugin ID string has been applied to your module.
   */
  val hasPluginId: Property<String>
}
