/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.core

import modular.core.internal.ModularJson
import modular.core.internal.StringEnum
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.api
import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.register
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
interface NamedLinkTypeContainer : NamedDomainObjectContainer<LinkTypeSpec> {
  @ModularDsl
  operator fun String.invoke(
    style: LinkStyle? = null,
    color: String? = null,
    displayName: String = this,
  ): NamedDomainObjectProvider<LinkTypeSpec> = register(this, style, color, displayName)
}

interface LinkTypeSpec {
  val name: String
  val configuration: Property<String>
  val style: Property<LinkStyle>
  val color: Property<String>
}

@KSerializable
data class LinkType(
  val configuration: String,
  val style: LinkStyle? = null,
  val color: String? = null,
  val displayName: String = configuration,
) : JSerializable

/**
 * These come from graphviz, but also work with mermaid/d2.
 * See https://graphviz.org/docs/attr-types/style/
 */
enum class LinkStyle(override val string: String) : StringEnum {
  Dashed("dashed"),
  Dotted("dotted"),
  Solid("solid"),
  Invis("invis"),
  Bold("bold"),
  ;

  override fun toString() = string
}
