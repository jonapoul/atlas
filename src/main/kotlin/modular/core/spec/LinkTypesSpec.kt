/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.spec

import modular.gradle.ModularDsl
import modular.graphviz.spec.StringEnum
import org.gradle.api.provider.SetProperty
import org.gradle.internal.impldep.org.intellij.lang.annotations.Language
import kotlin.text.RegexOption.IGNORE_CASE
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
 *   }
 * }
 * ```
 * You can create new types with the string invoke operator as above (similar to one used in Gradle dependencies
 * sometimes), or just call [add].
 *
 * Added entries are checked in priority order, so a configuration of `apiImplementationCompileOnly` in the example
 * above would match `api` but not reach `implementation` or `compileOnly`.
 */
interface LinkTypesSpec {
  val linkTypes: SetProperty<LinkType>

  @ModularDsl
  fun add(
    configuration: String,
    style: String? = null,
    color: String? = null,
    displayName: String = configuration,
  )

  @ModularDsl
  fun add(
    configuration: String,
    style: Style,
    color: String? = null,
    displayName: String = configuration,
  ) = add(configuration, style.string, color, displayName)

  @ModularDsl
  operator fun String.invoke(
    style: String? = null,
    color: String? = null,
    displayName: String = this,
  )

  @ModularDsl
  operator fun String.invoke(
    style: Style?,
    color: String? = null,
    displayName: String = this,
  ) = invoke(style?.string, color, displayName)

  @ModularDsl
  fun api(
    style: String? = null,
    color: String? = null,
    displayName: String = "api",
  ) = add(configuration = ".*?api", style, color, displayName)

  @ModularDsl
  fun api(
    style: Style,
    color: String? = null,
    displayName: String = "api",
  ) = api(style.string, color, displayName)

  @ModularDsl
  fun implementation(
    style: String? = null,
    color: String? = null,
    displayName: String = "implementation",
  ) = add(configuration = ".*?implementation", style, color, displayName)

  @ModularDsl
  fun implementation(
    style: Style,
    color: String? = null,
    displayName: String = "implementation",
  ) = implementation(style.string, color, displayName)
}

interface Style : StringEnum

@KSerializable
data class LinkType(
  val configuration: String,
  val style: String? = null,
  val color: String? = null,
  val displayName: String = configuration,
) : JSerializable
