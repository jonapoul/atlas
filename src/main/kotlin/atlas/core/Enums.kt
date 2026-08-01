package atlas.core

public interface StringEnum {
  public val string: String
}

public interface IntEnum {
  public val value: Int
}

/**
 * How a link between two projects is drawn. Not every framework supports every style - where one
 * doesn't, Atlas falls back to the closest match and warns you when the chart is configured:
 * - [Dotted] is drawn as [Dashed] by Mermaid.
 * - [Tapered] is only supported by Graphviz, and is drawn as [Solid] elsewhere.
 */
public enum class LinkStyle(override val string: String) : StringEnum {
  Solid("solid"),
  Bold("bold"),
  Dashed("dashed"),
  Dotted("dotted"),
  Invisible("invisible"),
  Tapered("tapered");

  /** The frameworks which can draw this style as-is. */
  public val supportedBy: Set<Framework>
    get() =
      when (this) {
        Solid,
        Bold,
        Dashed,
        Invisible -> setOf(Framework.D2, Framework.Graphviz, Framework.Mermaid)
        Dotted -> setOf(Framework.D2, Framework.Graphviz)
        Tapered -> setOf(Framework.Graphviz)
      }

  override fun toString(): String = string
}
