/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.core

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.internal.impldep.org.intellij.lang.annotations.Language

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
 *     registerByPathContains(name = "Domain", pathContains = "domain")
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
public interface NamedModuleTypeContainer<T : ModuleTypeSpec> : NamedDomainObjectContainer<T> {
  public fun registerByPluginId(
    name: String,
    pluginId: String,
    color: String? = null,
    extraConfig: T.() -> Unit = {},
  ): NamedDomainObjectProvider<T> = register(name) { type ->
    type.color.convention(color)
    type.hasPluginId.convention(pluginId)
    type.extraConfig()
  }

  public fun registerByPathMatches(
    name: String,
    @Language("RegExp") pathMatches: String,
    options: Set<RegexOption> = emptySet(),
    color: String? = null,
    extraConfig: T.() -> Unit = {},
  ): NamedDomainObjectProvider<T> = register(name) { type ->
    type.color.convention(color)
    type.pathMatches.convention(pathMatches)
    type.regexOptions.convention(options)
    type.extraConfig()
  }

  public fun registerByPathContains(
    name: String,
    pathContains: String,
    color: String? = null,
    extraConfig: T.() -> Unit = {},
  ): NamedDomainObjectProvider<T> = register(name) { type ->
    type.color.convention(color)
    type.pathContains.convention(pathContains)
    type.extraConfig()
  }
}

@ModularDsl
public interface ModuleTypeSpec : PropertiesSpec {
  /**
   * Required - this will be shown on your generated legend files.
   */
  public val name: String

  /**
   * Optional - defaults to #FFFFFF (white). Must be prefixed with a # followed by a 6-character hexadecimal string.
   */
  public val color: Property<String>

  /**
   * Checks against the path string of your module, e.g. ":path:to:my:module". This is case-sensitive.
   */
  public val pathContains: Property<String>

  /**
   * Similar to [pathContains] but more flexible with [Regex] pattern checking instead of straight string comparison.
   * Specify [regexOptions] if you want to get technical about it.
   */
  public val pathMatches: Property<String>
  public val regexOptions: SetProperty<RegexOption>

  /**
   * Checks whether the given plugin ID string has been applied to your module.
   */
  public val hasPluginId: Property<String>
}
