/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.spec

import modular.gradle.ModularDsl
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.SetProperty
import org.gradle.internal.impldep.kotlinx.serialization.Serializable
import org.gradle.internal.impldep.org.intellij.lang.annotations.Language

class ModuleNameSpec internal constructor(objects: ObjectFactory) {
  val replacements: SetProperty<Replacement> = objects.setProperty(Replacement::class.java)

  @ModularDsl
  fun replace(
    pattern: Regex,
    replacement: String,
  ) = replacements.add(Replacement(pattern, replacement))

  @ModularDsl
  fun replace(
    @Language("RegExp") pattern: String,
    replacement: String,
  ) = replacements.add(Replacement(pattern.toRegex(), replacement))
}

@Serializable
class Replacement(
  val pattern: Regex,
  val replacement: String,
) : java.io.Serializable
