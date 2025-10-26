@file:Suppress("unused") // public API

package atlas.core

import org.gradle.api.Action
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
 * atlas {
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
 * atlas {
 *   moduleTypes {
 *     // reference some built-in types
 *     androidApp()
 *     java {
 *       // custom overrides
 *       color = "black"
 *     }
 *
 *     // plus some manually-defined ones
 *     hasPluginId(name = "UI", color = "#ABC123", pluginId = "org.jetbrains.kotlin.plugin.compose")
 *     pathMatches(name = "Data", color = "#ABCDEF", pathMatches = ".*data$".toRegex())
 *     pathContains(name = "Domain", pathContains = "domain") {
 *       // plus some custom properties, depending on the plugin you've applied
 *     }
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
@AtlasDsl
public interface NamedModuleTypeContainer<T : ModuleTypeSpec> : NamedDomainObjectContainer<T> {
  public fun hasPluginId(
    name: String,
    pluginId: String,
    color: String? = null,
    action: Action<T>? = null,
  ): NamedDomainObjectProvider<T> = register(name) { type ->
    type.color.convention(color)
    type.hasPluginId.convention(pluginId)
    action?.execute(type)
  }

  public fun pathMatches(
    name: String,
    @Language("RegExp") pathMatches: String,
    options: Set<RegexOption> = emptySet(),
    color: String? = null,
    action: Action<T>? = null,
  ): NamedDomainObjectProvider<T> = register(name) { type ->
    type.color.convention(color)
    type.pathMatches.convention(pathMatches)
    type.regexOptions.convention(options)
    action?.execute(type)
  }

  public fun pathContains(
    name: String,
    pathContains: String,
    color: String? = null,
    action: Action<T>? = null,
  ): NamedDomainObjectProvider<T> = register(name) { type ->
    type.color.convention(color)
    type.pathContains.convention(pathContains)
    action?.execute(type)
  }
}

@AtlasDsl
public interface ModuleTypeSpec : PropertiesSpec {
  /**
   * Required - this will be shown on your generated legend files.
   */
  public val name: String

  /**
   * Optional. Must be a valid CSS color string.
   */
  public val color: Property<String>

  /**
   * Checks against the path string of your module, e.g. ":path:to:my:module". This is case-sensitive.
   */
  public val pathContains: Property<String>

  /**
   * Similar to [pathContains] but more flexible with [Regex] pattern checking instead of straight string comparison.
   */
  public val pathMatches: Property<String>

  /**
   * Options to use when matching [pathMatches]. Defaults to empty set, which is case-sensitive matching. Unused
   * unless [pathMatches] is set.
   */
  public val regexOptions: SetProperty<RegexOption>

  /**
   * Checks whether the given plugin ID string has been applied to your module.
   */
  public val hasPluginId: Property<String>
}
