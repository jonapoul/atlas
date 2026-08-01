package atlas.core

import org.gradle.api.provider.MapProperty

/**
 * Shared by [ProjectTypeSpec] and [LinkTypeSpec] - the specs which are declared once and then
 * rendered by every framework you've configured.
 *
 * Most properties on those specs only mean something to one framework: [ProjectTypeSpec.render3D]
 * is a D2 concept, [ProjectTypeSpec.peripheries] is a Graphviz one. You can set all of them
 * regardless of which frameworks you've enabled - the ones which don't apply are ignored, and Atlas
 * logs a warning naming each property that no configured framework will use.
 *
 * Use [put] to set an attribute Atlas doesn't have a property for yet, e.g. if the framework adds
 * one before this plugin catches up:
 * ```kotlin
 * atlas {
 *   projectTypes {
 *     kotlinJvm {
 *       put(Framework.D2, key = "style.fill", value = "red")
 *     }
 *   }
 * }
 * ```
 */
@AtlasDsl
public interface StyleSpec {
  /** The raw attributes which will be handed to [framework] when writing charts. */
  public fun properties(framework: Framework): MapProperty<String, String>

  /** Sets a single raw attribute for [framework]. */
  public fun put(framework: Framework, key: String, value: Any): Unit =
    properties(framework).put(key, value.toString())

  /** Removes every attribute set on this spec, for all frameworks. */
  public fun clear()

  /** Removes every attribute set on this spec for [framework] only. */
  public fun clear(framework: Framework)
}
