/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("ktlint:standard:parameter-list-wrapping")

package modular.spec

import modular.gradle.ModularDsl
import modular.internal.Replacement
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty
import org.gradle.internal.impldep.org.intellij.lang.annotations.Language

/**
 * API for modifying module names when inserting them into any generated diagrams. For example if your modules are
 * within a heavily-nested `"modules"` directory in your project's root, you might want to call something like:
 *
 * ```kotlin
 * modular {
 *   modulePathTransforms {
 *     remove("^:modules:")
 *     replace(":", " ")
 *   }
 * }
 * ```
 *
 * then a path of `":modules:path:to:module"` will be mapped to `"path to module"`. Remember the declarations inside
 * `modulePathTransforms` are called in descending order.
 *
 * Regex groups aren't supported, just standard matches.
 */
interface ModulePathTransformSpec : SetProperty<Replacement> {
  @ModularDsl override fun add(element: Replacement)
  @ModularDsl override fun add(provider: Provider<out Replacement>)
  @ModularDsl override fun addAll(elements: Iterable<Replacement>)
  @ModularDsl override fun addAll(vararg elements: Replacement)
  @ModularDsl override fun addAll(provider: Provider<out Iterable<Replacement>>)

  @ModularDsl fun remove(@Language("RegExp") pattern: String)
  @ModularDsl fun remove(pattern: Regex)
  @ModularDsl fun replace(@Language("RegExp") pattern: String, replacement: String)
  @ModularDsl fun replace(pattern: Regex, replacement: String)
}
