/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("ktlint:standard:parameter-list-wrapping")

package modular.core

import modular.core.internal.RegexSerializer
import org.gradle.api.provider.SetProperty
import org.gradle.internal.impldep.org.intellij.lang.annotations.Language
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

/**
 * API for modifying module names when inserting them into any generated diagrams. For example if your modules are
 * within a heavily-nested `"modules"` directory in your project's root, you might want to call something like:
 *
 * ```kotlin
 * modular {
 *   pathTransforms {
 *     remove("^:modules:")
 *     replace(":", " ")
 *   }
 * }
 * ```
 *
 * then a path of `":modules:path:to:module"` will be mapped to `"path to module"` for display in the charts.
 * Remember the declarations inside `pathTransforms` are called in descending order.
 */
@ModularDsl
public interface PathTransformSpec {
  public val replacements: SetProperty<Replacement>

  public fun remove(@Language("RegExp") pattern: String)
  public fun remove(pattern: Regex)
  public fun replace(@Language("RegExp") pattern: String, replacement: String)
  public fun replace(pattern: Regex, replacement: String)
}

@KSerializable
public class Replacement(
  @KSerializable(RegexSerializer::class) public val pattern: Regex,
  public val replacement: String,
) : JSerializable
