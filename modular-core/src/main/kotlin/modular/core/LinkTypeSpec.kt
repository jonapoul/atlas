/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.core

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.provider.Property
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

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
@ModularDsl
interface NamedLinkTypeContainer : NamedDomainObjectContainer<LinkTypeSpec>

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
