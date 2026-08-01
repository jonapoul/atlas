@file:Suppress("unused", "TooManyFunctions") // public API

package atlas.core

import atlas.d2.FillPattern
import atlas.d2.Font
import atlas.d2.Shape as D2Shape
import atlas.d2.TextTransform
import atlas.graphviz.ImagePos
import atlas.graphviz.NodeStyle
import atlas.graphviz.Shape as GraphvizShape
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.internal.impldep.org.intellij.lang.annotations.Language

/**
 * Represents a category of project that you can use to match against those in your project. You can
 * use some of the built-in example types like below:
 * ```kotlin
 * atlas {
 *   projectTypes {
 *     androidApp()
 *     androidLibrary()
 *     java()
 *     kotlinJvm()
 *     kotlinMultiplatform()
 *     other()
 *
 *     // or use useDefaults() to include all of the above
 *   }
 * }
 * ```
 *
 * or create custom types like:
 * ```kotlin
 * atlas {
 *   projectTypes {
 *     // reference some built-in types
 *     androidApp()
 *     java {
 *       // custom overrides
 *       color = "black"
 *     }
 *
 *     // plus some manually-defined ones
 *     hasPluginId(name = "UI", color = "#ABC123", pluginId = "org.jetbrains.kotlin.plugin.compose")
 *     pathMatches(name = "Data", color = "#ABCDEF", pathMatches = ".*data$".toRegex())
 *     pathContains(name = "Domain", pathContains = "domain") {
 *       // plus any style properties from the frameworks you've configured
 *     }
 *   }
 * }
 * ```
 *
 * Remember that priority is given in descending order, so in the example above the UI project type
 * will be checked before the data or domain types.
 *
 * Exactly one of [ProjectTypeSpec.pathContains], [ProjectTypeSpec.pathMatches] or
 * [ProjectTypeSpec.hasPluginId] must be set. If not, Gradle will:
 * - warn you during IDE sync, or
 * - fail when running any tasks which reference them.
 */
@AtlasDsl
public interface NamedProjectTypeContainer : NamedDomainObjectContainer<ProjectTypeSpec> {
  public fun hasPluginId(
    name: String,
    pluginId: String,
    color: String? = null,
    action: Action<ProjectTypeSpec>? = null,
  ): NamedDomainObjectProvider<ProjectTypeSpec> =
    register(name) { type ->
      type.color.convention(color)
      type.hasPluginId.convention(pluginId)
      action?.execute(type)
    }

  public fun pathMatches(
    name: String,
    @Language("RegExp") pathMatches: String,
    options: Set<RegexOption> = emptySet(),
    color: String? = null,
    action: Action<ProjectTypeSpec>? = null,
  ): NamedDomainObjectProvider<ProjectTypeSpec> =
    register(name) { type ->
      type.color.convention(color)
      type.pathMatches.convention(pathMatches)
      type.regexOptions.convention(options)
      action?.execute(type)
    }

  public fun pathContains(
    name: String,
    pathContains: String,
    color: String? = null,
    action: Action<ProjectTypeSpec>? = null,
  ): NamedDomainObjectProvider<ProjectTypeSpec> =
    register(name) { type ->
      type.color.convention(color)
      type.pathContains.convention(pathContains)
      action?.execute(type)
    }
}

/**
 * A category of project, plus how its node should be drawn.
 *
 * The matchers ([pathContains], [pathMatches], [hasPluginId]) and [color] apply to every framework.
 * The style properties below are grouped by which framework reads them - you can set any of them
 * whether or not that framework is configured, but Atlas will warn you about any which no
 * configured framework will read. See [StyleSpec] for more on that, and for [StyleSpec.put] to set
 * attributes which don't have a property here yet.
 */
@AtlasDsl
public interface ProjectTypeSpec : StyleSpec {
  /** Required - this will be shown on your generated legend files. */
  public val name: String

  /**
   * Optional. Must be a valid CSS color string. Used as the node's fill color by every framework,
   * unless overridden by the more specific [fill].
   */
  public val color: Property<String>

  /**
   * Checks against the path string of your project, e.g. ":path:to:my:project". This is
   * case-sensitive.
   */
  public val pathContains: Property<String>

  /**
   * Similar to [pathContains] but more flexible with [Regex] pattern checking instead of straight
   * string comparison.
   */
  public val pathMatches: Property<String>

  /**
   * Options to use when matching [pathMatches]. Defaults to empty set, which is case-sensitive
   * matching. Unused unless [pathMatches] is set.
   */
  public val regexOptions: SetProperty<RegexOption>

  /** Checks whether the given plugin ID string has been applied to your project. */
  public val hasPluginId: Property<String>

  // -----------------------------------------------------------------------------------------
  // Read by every framework
  // -----------------------------------------------------------------------------------------

  /** The node's background color. Overrides [color]. */
  public var fill: String?

  /** The node's border color. */
  public var stroke: String?

  /** The node's border width. */
  public var strokeWidth: String?

  /** The color of the node's label text. */
  public var fontColor: String?

  /** The size of the node's label text. */
  public var fontSize: String?

  // -----------------------------------------------------------------------------------------
  // D2 and Mermaid
  // -----------------------------------------------------------------------------------------

  /** How see-through the node is, between 0 and 1. */
  public var opacity: Float?

  // -----------------------------------------------------------------------------------------
  // D2 only - see [the D2 docs](https://d2lang.com/tour/style)
  // -----------------------------------------------------------------------------------------

  public var animated: Boolean?

  public var bold: Boolean?

  public var borderRadius: Int?

  /** Only applicable to [D2Shape.Rectangle] and [D2Shape.Oval]. */
  public var doubleBorder: Boolean?

  public var fillPattern: FillPattern?

  public var font: Font?

  public var italic: Boolean?

  public var multiple: Boolean?

  /** Only applicable to [D2Shape.Rectangle] and [D2Shape.Square]. */
  public var render3D: Boolean?

  public var shadow: Boolean?

  /** The shape of the node in D2 charts. See [graphvizShape] for the Graphviz equivalent. */
  public var d2Shape: D2Shape?

  public var strokeDash: Int?

  public var textTransform: TextTransform?

  public var underline: Boolean?

  // -----------------------------------------------------------------------------------------
  // Mermaid only - see
  // [the Mermaid docs](https://mermaid.js.org/syntax/flowchart.html#styling-a-node)
  // -----------------------------------------------------------------------------------------

  /** An array of integers, like `"5 5"`. */
  public var strokeDashArray: String?

  // -----------------------------------------------------------------------------------------
  // Graphviz only - see [the Graphviz docs](https://graphviz.org/docs/nodes/). Atlas won't
  // validate any of these, it just passes them through and lets Graphviz complain.
  // -----------------------------------------------------------------------------------------

  /** The shape of the node in Graphviz charts. See [d2Shape] for the D2 equivalent. */
  public var graphvizShape: GraphvizShape?

  public var colorScheme: String?

  public var comment: String?

  public var distortion: String?

  public var fixedSize: String?

  public var fontName: String?

  public var gradientAngle: Int?

  public var group: String?

  public var height: Number?

  public var href: String?

  public var id: String?

  public var image: String?

  public var imagePos: ImagePos?

  public var imageScale: String?

  public var label: String?

  public var labelLoc: String?

  public var layer: String?

  public var margin: String?

  public var noJustify: Boolean?

  public var ordering: String?

  public var orientation: Number?

  public var peripheries: Int?

  public var pin: Boolean?

  public var pos: String?

  public var rects: String?

  public var regular: Boolean?

  public var root: String?

  public var samplePoints: Int?

  public var shapeFile: String?

  public var showBoxes: Int?

  public var sides: Int?

  public var skew: Number?

  public var sortv: Int?

  public var style: NodeStyle?

  public var target: String?

  public var tooltip: String?

  public var url: String?

  public var vertices: String?

  public var width: Number?

  public var xlabel: String?

  public var xlp: String?

  public var z: Number?
}
