@file:Suppress("unused") // public API

package org.gradle.kotlin.dsl

import atlas.core.NamedProjectTypeContainer
import atlas.core.ProjectTypeSpec
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectProvider

/**
 * Adds an "Android App" project type, with a lime green color.
 */
@JvmOverloads
public fun <T : ProjectTypeSpec> NamedProjectTypeContainer<T>.androidApp(
  name: String = "Android App",
  color: String? = "limegreen",
  action: Action<T>? = null,
): NamedDomainObjectProvider<T> = hasPluginId(
  name = name,
  color = color,
  pluginId = "com.android.application",
  action = action,
)

/**
 * Adds an "Android Library" project type, with a lighter green color.
 */
@JvmOverloads
public fun <T : ProjectTypeSpec> NamedProjectTypeContainer<T>.androidLibrary(
  name: String = "Android Library",
  color: String? = "lightgreen",
  action: Action<T>? = null,
): NamedDomainObjectProvider<T> = hasPluginId(
  name = name,
  color = color,
  pluginId = "com.android.library",
  action = action,
)

/**
 * Adds a "Java" project type, with an orange color.
 */
@JvmOverloads
public fun <T : ProjectTypeSpec> NamedProjectTypeContainer<T>.java(
  name: String = "Java",
  color: String? = "orange",
  action: Action<T>? = null,
): NamedDomainObjectProvider<T> = hasPluginId(
  name = name,
  color = color,
  pluginId = "java",
  action = action,
)

/**
 * Adds a "Kotlin JVM" project type, with a darkish pink color.
 */
@JvmOverloads
public fun <T : ProjectTypeSpec> NamedProjectTypeContainer<T>.kotlinJvm(
  name: String = "Kotlin JVM",
  color: String? = "mediumorchid",
  action: Action<T>? = null,
): NamedDomainObjectProvider<T> = hasPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.jvm",
  action = action,
)

/**
 * Adds a "Kotlin Multiplatform" project type, with a purplish blue color.
 */
@JvmOverloads
public fun <T : ProjectTypeSpec> NamedProjectTypeContainer<T>.kotlinMultiplatform(
  name: String = "Kotlin Multiplatform",
  color: String? = "mediumslateblue",
  action: Action<T>? = null,
): NamedDomainObjectProvider<T> = hasPluginId(
  name = name,
  color = color,
  pluginId = "org.jetbrains.kotlin.multiplatform",
  action = action,
)

/**
 * Adds an "Other" project type, with a grey color. This will match anything and everything, so make sure to declare
 * this last in the projectTypes block from your build file.
 */
@JvmOverloads
public fun <T : ProjectTypeSpec> NamedProjectTypeContainer<T>.other(
  name: String = "Other",
  color: String? = "gainsboro",
  action: Action<T>? = null,
): NamedDomainObjectProvider<T> = pathMatches(
  name = name,
  color = color,
  pathMatches = ".*?", // match anything and everything - should always have this declared last
  action = action,
)

/**
 * Adds a set of standard project types to your config.
 */
public fun <T : ProjectTypeSpec> NamedProjectTypeContainer<T>.useDefaults() {
  // Highest priority ↓↓
  androidApp()
  kotlinMultiplatform()
  androidLibrary()
  kotlinJvm()
  java()
  other()
  // Lowest priority ↑↑
}
