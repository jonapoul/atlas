@file:Suppress("unused", "TooManyFunctions") // public API

package atlas.core

import atlas.d2.Font
import atlas.d2.TextTransform
import atlas.graphviz.ArrowType
import atlas.graphviz.Dir
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.provider.Property

/**
 * Used to configure expected link "types" between your projects. The majority of the time, these
 * are only ever going to be [NamedLinkTypeContainer.api] or
 * [NamedLinkTypeContainer.implementation], hence those being listed for easier access. Configure
 * like:
 * ```kotlin
 * atlas {
 *   linkTypes {
 *     api(color = "green")
 *     implementation(color = "#5555FF")
 *     "compileOnly"(style = LinkStyle.Dotted, displayName = "Compile Only")
 *     "^withRegex.*"(style = LinkStyle.Dashed, displayName = "Supports case-insensitive regex")
 *   }
 * }
 * ```
 *
 * You can create new types with the string invoke operator as above (similar to one used in Gradle
 * dependencies sometimes), or just call one of the [register] overloads.
 *
 * Added entries are checked in priority order, so a configuration of `apiImplementationCompileOnly`
 * in the example above would match `api` but not reach `implementation` or `compileOnly`.
 */
@AtlasDsl
public interface NamedLinkTypeContainer : NamedDomainObjectContainer<LinkTypeSpec> {
  public fun register(
    configuration: String,
    style: LinkStyle? = null,
    color: String? = null,
    displayName: String = configuration,
    action: Action<LinkTypeSpec>? = null,
  ): NamedDomainObjectProvider<LinkTypeSpec> =
    register(displayName) { spec ->
      spec.configuration.set(configuration)
      spec.style.set(style)
      spec.color.set(color)
      action?.execute(spec)
    }

  public fun api(
    style: LinkStyle? = null,
    color: String? = null,
    displayName: String = "api",
    action: Action<LinkTypeSpec>? = null,
  ): NamedDomainObjectProvider<LinkTypeSpec> =
    register(
      configuration = ".*?api",
      style = style,
      color = color,
      displayName = displayName,
      action = action,
    )

  public fun implementation(
    style: LinkStyle? = null,
    color: String? = null,
    displayName: String = "implementation",
    action: Action<LinkTypeSpec>? = null,
  ): NamedDomainObjectProvider<LinkTypeSpec> =
    register(
      configuration = ".*?implementation",
      style = style,
      color = color,
      displayName = displayName,
      action = action,
    )

  public operator fun String.invoke(
    style: LinkStyle? = null,
    color: String? = null,
    displayName: String = this,
    action: Action<LinkTypeSpec>? = null,
  ): NamedDomainObjectProvider<LinkTypeSpec> =
    register(
      configuration = this,
      style = style,
      color = color,
      displayName = displayName,
      action = action,
    )
}

/**
 * A category of link between two projects, plus how it should be drawn.
 *
 * [configuration], [style] and [color] apply to every framework. The style properties below are
 * grouped by which framework reads them - you can set any of them whether or not that framework is
 * configured, but Atlas will warn you about any which no configured framework will read. See
 * [StyleSpec] for more on that, and for [StyleSpec.put] to set attributes which don't have a
 * property here yet.
 */
@AtlasDsl
public interface LinkTypeSpec : StyleSpec {
  /** Shown on your generated legend files. */
  public val name: String

  /** Regex matched against the Gradle configuration name which created the link, e.g. "api". */
  public val configuration: Property<String>

  /** How the line is drawn. Defaults to [LinkStyle.Solid]. */
  public val style: Property<LinkStyle>

  /** The color of the line. Overridden by the more specific [stroke]. */
  public val color: Property<String>

  // -----------------------------------------------------------------------------------------
  // Read by every framework
  // -----------------------------------------------------------------------------------------

  /** The color of the line. Overrides [color]. */
  public var stroke: String?

  /** The width of the line. */
  public var strokeWidth: String?

  /** The color of the link's label text, if [AtlasExtension.displayLinkLabels] is enabled. */
  public var fontColor: String?

  // -----------------------------------------------------------------------------------------
  // D2 and Graphviz
  // -----------------------------------------------------------------------------------------

  /** The size of the link's label text, if [AtlasExtension.displayLinkLabels] is enabled. */
  public var fontSize: String?

  // -----------------------------------------------------------------------------------------
  // D2 and Mermaid
  // -----------------------------------------------------------------------------------------

  /** How see-through the line is, between 0 and 1. */
  public var opacity: Float?

  // -----------------------------------------------------------------------------------------
  // D2 only - see [the D2 docs](https://d2lang.com/tour/style)
  // -----------------------------------------------------------------------------------------

  public var animated: Boolean?

  public var bold: Boolean?

  public var borderRadius: Int?

  public var font: Font?

  public var italic: Boolean?

  public var strokeDash: Int?

  public var textTransform: TextTransform?

  public var underline: Boolean?

  // -----------------------------------------------------------------------------------------
  // Mermaid only - see
  // [the Mermaid docs](https://mermaid.js.org/syntax/flowchart.html#links-between-nodes)
  // -----------------------------------------------------------------------------------------

  /** An array of integers, like `"5 5"`. */
  public var strokeDashArray: String?

  // -----------------------------------------------------------------------------------------
  // Graphviz only - see [the Graphviz docs](https://graphviz.org/docs/edges/). Atlas won't
  // validate any of these, it just passes them through and lets Graphviz complain.
  // -----------------------------------------------------------------------------------------

  public var arrowHead: ArrowType?

  public var arrowSize: Number?

  public var arrowTail: ArrowType?

  public var colorScheme: String?

  public var comment: String?

  public var constraint: Boolean?

  public var decorate: Boolean?

  public var dir: Dir?

  public var edgeHref: String?

  public var edgeTarget: String?

  public var edgeTooltip: String?

  public var edgeUrl: String?

  public var fillColor: String?

  public var fontName: String?

  public var headLp: String?

  public var headClip: Boolean?

  public var headHref: String?

  public var headLabel: String?

  public var headPort: String?

  public var headTarget: String?

  public var headTooltip: String?

  public var headUrl: String?

  public var href: String?

  public var id: String?

  public var label: String?

  public var labelAngle: Number?

  public var labelDistance: Number?

  public var labelFloat: Boolean?

  public var labelFontColor: String?

  public var labelFontName: String?

  public var labelFontSize: String?

  public var labelHref: String?

  public var labelTarget: String?

  public var labelTooltip: String?

  public var labelUrl: String?

  public var layer: String?

  public var len: Number?

  public var lhead: String?

  public var lp: String?

  public var ltail: String?

  public var minLen: Int?

  public var noJustify: Boolean?

  public var pos: String?

  public var sameHead: String?

  public var sameTail: String?

  public var showBoxes: Int?

  public var tailLp: String?

  public var tailClip: Boolean?

  public var tailHref: String?

  public var tailLabel: String?

  public var tailPort: String?

  public var tailTarget: String?

  public var tailTooltip: String?

  public var tailUrl: String?

  public var target: String?

  public var tooltip: String?

  public var url: String?

  public var weight: Number?

  public var xLabel: String?

  public var xlp: String?
}

@KSerializable
public data class LinkType(
  public val configuration: String,
  public val style: LinkStyle? = null,
  public val color: String? = null,
  public val displayName: String = configuration,
  public val properties: Map<String, Map<String, String>> = emptyMap(),
) : JSerializable {
  /** The attributes which [framework] should apply to this link. */
  public fun properties(framework: Framework): Map<String, String> =
    properties[framework.string].orEmpty()
}
