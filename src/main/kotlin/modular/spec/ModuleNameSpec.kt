/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.gradle.ModularDsl
import org.gradle.api.provider.ListProperty
import org.gradle.internal.impldep.kotlinx.serialization.Serializable
import org.gradle.internal.impldep.org.intellij.lang.annotations.Language

interface ModuleNameSpec {
  val replacements: ListProperty<Replacement>

  @ModularDsl
  fun remove(
    @Language("RegExp") pattern: String,
  )

  @ModularDsl
  fun remove(pattern: Regex)

  @ModularDsl
  fun replace(
    @Language("RegExp") pattern: String,
    replacement: String,
  )

  @ModularDsl
  fun replace(
    pattern: Regex,
    replacement: String,
  )
}

@Serializable
class Replacement(
  val pattern: Regex,
  val replacement: String,
) : java.io.Serializable
