/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.gradle.ExperimentalModularApi
import modular.gradle.ModularDsl
import org.gradle.api.provider.SetProperty
import org.gradle.internal.impldep.org.intellij.lang.annotations.Language
import kotlin.text.RegexOption.IGNORE_CASE
import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable
import java.io.Serializable as JSerializable

/**
 * Used to configure expected link "types" between your modules. The majority of the time, these are only ever
 * going to be [api] or [implementation], hence those being listed for easier access. Configure like:
 *
 * ```kotlin
 * modular {
 *   graphViz {
 *     linkTypes {
 *       api(color = "green")
 *       implementation(color = "#5555FF")
 *       "compileOnly"(style = "dotted")
 *     }
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
    configuration: Regex,
    style: String? = null,
    color: String? = null,
  )

  @ModularDsl
  fun add(
    @Language("RegExp") configuration: String,
    style: String? = null,
    color: String? = null,
  ) = add(configuration.toRegex(), style, color)

  @ExperimentalModularApi
  @ModularDsl
  operator fun String.invoke(
    style: String? = null,
    color: String? = null,
  )

  @ModularDsl
  fun api(
    style: String? = null,
    color: String? = null,
  ) = add(
    configuration = ".*?api".toRegex(IGNORE_CASE),
    style = style,
    color = color,
  )

  @ModularDsl
  fun implementation(
    style: String? = null,
    color: String? = null,
  ) = add(
    configuration = ".*?implementation".toRegex(IGNORE_CASE),
    style = style,
    color = color,
  )
}

@KSerializable
data class LinkType(
  val configuration: Regex,
  val style: String? = null,
  val color: String? = null,
) : JSerializable
