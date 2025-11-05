@file:Suppress("ktlint:standard:parameter-list-wrapping")

package atlas.core

import atlas.core.internal.RegexSerializer
import org.gradle.api.provider.SetProperty
import org.gradle.internal.impldep.org.intellij.lang.annotations.Language
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

/**
 * API for modifying project names when inserting them into any generated diagrams. For example if your projects are
 * within a heavily-nested `"projects"` directory in your project's root, you might want to call something like:
 *
 * ```kotlin
 * atlas {
 *   pathTransforms {
 *     remove("^:projects:")
 *     replace(pattern = ":", replacement = " ")
 *   }
 * }
 * ```
 *
 * then a path of `":projects:path:to:something"` will be mapped to `"path to something"` for display in the charts.
 * Remember the declarations inside `pathTransforms` are called in descending order.
 *
 * It doesn't support Regex group replacement, just pattern identification.
 */
@AtlasDsl
public interface PathTransformSpec {
  public val replacements: SetProperty<Replacement>

  public fun remove(@Language("RegExp") pattern: String)
  public fun remove(pattern: Regex)
  public fun replace(@Language("RegExp") pattern: String, replacement: String)
  public fun replace(pattern: Regex, replacement: String)
}

/**
 * Model class for a [PathTransformSpec].
 */
@KSerializable
public class Replacement(
  @KSerializable(RegexSerializer::class) public val pattern: Regex,
  public val replacement: String,
) : JSerializable
