/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.spec

import modular.core.ModularDsl
import modular.core.internal.StringEnum
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.api
import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.register
import java.io.Serializable as JSerializable
import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable

/**
 * Used to configure expected link "types" between your modules. The majority of the time, these are only ever
 * going to be [api] or [implementation], hence those being listed for easier access. Configure like:
 *
 * ```kotlin
 * modular {
 *   linkTypes {
 *     api(color = "green")
 *     implementation(color = "#5555FF")
 *     "compileOnly"(style = "dotted", displayName = "Compile Only")
 *     "^withRegex.*"(style = "dashed", displayName = "Supports case-insensitive regex patterns")
 *   }
 * }
 * ```
 * You can create new types with the string invoke operator as above (similar to one used in Gradle dependencies
 * sometimes), or just call one of the [register] overloads.
 *
 * Added entries are checked in priority order, so a configuration of `apiImplementationCompileOnly` in the example
 * above would match `api` but not reach `implementation` or `compileOnly`.
 */
interface NamedLinkTypeContainer : NamedDomainObjectContainer<LinkTypeSpec> {
  @ModularDsl
  operator fun String.invoke(
    style: String? = null,
    color: String? = null,
    displayName: String = this,
  ): NamedDomainObjectProvider<LinkTypeSpec> = register(this, style, color, displayName)

  @ModularDsl
  operator fun String.invoke(
    style: StringEnum?,
    color: String? = null,
    displayName: String = this,
  ): NamedDomainObjectProvider<LinkTypeSpec> = register(this, style?.string, color, displayName)
}

interface LinkTypeSpec {
  val name: String
  val configuration: Property<String>
  val style: Property<String>
  val color: Property<String>
}

@KSerializable
data class LinkType(
  val configuration: String,
  val style: String? = null,
  val color: String? = null,
  val displayName: String = configuration,
) : JSerializable
