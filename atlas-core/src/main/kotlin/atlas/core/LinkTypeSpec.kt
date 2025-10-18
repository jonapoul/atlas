/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package atlas.core

import atlas.core.internal.StringEnum
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.provider.Property
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

/**
 * Used to configure expected link "types" between your modules. The majority of the time, these are only ever
 * going to be [NamedLinkTypeContainer.api] or [NamedLinkTypeContainer.implementation], hence those being listed for
 * easier access. Configure like:
 *
 * ```kotlin
 * atlas {
 *   linkTypes {
 *     api(color = "green")
 *     implementation(color = "#5555FF")
 *     "compileOnly"(style = Dotted, displayName = "Compile Only")
 *     "^withRegex.*"(style = Dashed, displayName = "Supports case-insensitive regex patterns")
 *   }
 * }
 * ```
 * You can create new types with the string invoke operator as above (similar to one used in Gradle dependencies
 * sometimes), or just call one of the [register] overloads.
 *
 * Added entries are checked in priority order, so a configuration of `apiImplementationCompileOnly` in the example
 * above would match `api` but not reach `implementation` or `compileOnly`.
 */
@AtlasDsl
public interface NamedLinkTypeContainer<T : LinkTypeSpec> : NamedDomainObjectContainer<T> {
  public fun register(
    configuration: String,
    style: LinkStyle? = null,
    color: String? = null,
    displayName: String = configuration,
    action: Action<T>? = null,
  ): NamedDomainObjectProvider<T> = register(displayName) { spec ->
    spec.configuration.set(configuration)
    spec.style.set(style?.string)
    spec.color.set(color)
    action?.execute(spec)
  }

  public fun api(
    style: LinkStyle? = null,
    color: String? = null,
    displayName: String = "api",
    action: Action<T>? = null,
  ): NamedDomainObjectProvider<T> = register(
    configuration = ".*?api",
    style = style,
    color = color,
    displayName = displayName,
    action = action,
  )

  public fun implementation(
    style: LinkStyle? = null,
    color: String? = null,
    displayName: String = "implementation",
    action: Action<T>? = null,
  ): NamedDomainObjectProvider<T> = register(
    configuration = ".*?implementation",
    style = style,
    color = color,
    displayName = displayName,
    action = action,
  )

  public operator fun String.invoke(
    style: LinkStyle? = null,
    color: String? = null,
    displayName: String = this,
    action: Action<T>? = null,
  ): NamedDomainObjectProvider<T> = register(
    configuration = this,
    style = style,
    color = color,
    displayName = displayName,
    action = action,
  )
}

public interface LinkStyle : StringEnum

@AtlasDsl
public interface LinkTypeSpec : PropertiesSpec {
  public val name: String
  public val configuration: Property<String>
  public val style: Property<String>
  public val color: Property<String>
}

@KSerializable
public data class LinkType(
  public val configuration: String,
  public val style: String? = null,
  public val color: String? = null,
  public val displayName: String = configuration,
  public val properties: Map<String, String> = emptyMap(),
) : JSerializable
