@file:Suppress("unused", "MagicNumber") // public API

package atlas.d2

import atlas.core.IntEnum
import atlas.core.StringEnum
import atlas.core.LinkStyle as CoreLinkStyle

/**
 * See [https://d2lang.com/tour/shapes/](https://d2lang.com/tour/shapes/)
 */
public enum class Shape(override val string: String) : StringEnum {
  Rectangle("rectangle"),
  Square("square"),
  Page("page"),
  Parallelogram("parallelogram"),
  Document("document"),
  Cylinder("cylinder"),
  Queue("queue"),
  Package("package"),
  Step("step"),
  Callout("callout"),
  StoredData("stored_data"),
  Person("person"),
  Diamond("diamond"),
  Oval("oval"),
  Circle("circle"),
  Hexagon("hexagon"),
  Cloud("cloud"),
  C4Person("c4-person"),
  ;

  override fun toString(): String = string
}

/**
 * See [https://d2lang.com/tour/connections/](https://d2lang.com/tour/connections/)
 */
public enum class LinkStyle(override val string: String) : CoreLinkStyle {
  Basic("basic"),
  Bold("bold"),
  Dashed("dashed"),
  Dotted("dotted"),
  Invisible("invisible"),
  ;

  override fun toString(): String = string
}

/**
 * See [https://d2lang.com/tour/layouts/](https://d2lang.com/tour/layouts/)
 */
public enum class LayoutEngine(override val string: String) : StringEnum {
  /**
   * See [the D2 docs](https://d2lang.com/tour/dagre/) for official documentation, and [D2DagreSpec]
   * for Atlas configuration of it.
   */
  Dagre("dagre"),

  /**
   * See [the D2 docs](https://d2lang.com/tour/elk/) for official documentation, and [D2ElkSpec]
   * for Atlas configuration of it.
   */
  Elk("elk"),

  /**
   * See [the D2 docs](https://d2lang.com/tour/tala/) for official documentation, and [D2TalaSpec]
   * for Atlas configuration of it.
   */
  Tala("tala"),
  ;

  override fun toString(): String = string
}

/**
 * [https://d2lang.com/tour/themes/](https://d2lang.com/tour/themes/)
 */
public enum class Theme(override val value: Int) : IntEnum {
  Default(0),
  NeutralGrey(1),
  FlagshipTerrastruct(3),
  CoolClassics(4),
  MixedBerryBlue(5),
  GrapeSoda(6),
  Aubergine(7),
  ColorblindClear(8),
  VanillaNitroCola(100),
  OrangeCreamsicle(101),
  ShirleyTemple(102),
  EarthTones(103),
  EvergladeGreen(104),
  ButteredToast(105),
  Terminal(300),
  TerminalGrayscale(301),
  Origami(302),
  C4(303),

  // These two are intended for configuration as dark themes
  DarkMauve(200),
  DarkFlagshipTerrastruct(201),
}

/**
 * [https://d2lang.com/tour/exports/](https://d2lang.com/tour/exports/)
 */
public enum class FileFormat(override val string: String) : StringEnum {
  Svg("svg"),
  Png("png"),
  Pdf("pdf"),
  Pptx("pptx"),
  Gif("gif"),
  Ascii("txt"),
  ;

  override fun toString(): String = string
}

/**
 * [https://d2lang.com/tour/layouts/#direction](https://d2lang.com/tour/layouts/#direction)
 */
public enum class Direction(override val string: String) : StringEnum {
  Up("up"),
  Down("down"),
  Right("right"),
  Left("left"),
  ;

  override fun toString(): String = string
}

/**
 * [https://d2lang.com/tour/style/#fill-pattern](https://d2lang.com/tour/style/#fill-pattern)
 */
public enum class FillPattern(override val string: String) : StringEnum {
  Dots("dots"),
  Lines("lines"),
  Grain("grain"),
  None("none"),
  ;

  override fun toString(): String = string
}

/**
 * [https://d2lang.com/tour/connections/#arrowheads](https://d2lang.com/tour/connections/#arrowheads)
 */
public enum class ArrowType(override val string: String) : StringEnum {
  Triangle("triangle"),
  Arrow("arrow"),
  Diamond("diamond"),
  Circle("circle"),
  Box("box"),
  CrowsFootOne("cf-one"),
  CrowsFootOneRequired("cf-one-required"),
  CrowsFootMany("cf-many"),
  CrowsFootManyRequired("cf-many-required"),
  Cross("cross"),
  ;

  override fun toString(): String = string
}

/**
 * [https://d2lang.com/tour/positions/](https://d2lang.com/tour/positions/)
 */
public enum class Position(override val string: String) : StringEnum {
  TopLeft("top-left"),
  TopCenter("top-center"),
  TopRight("top-right"),
  CenterLeft("center-left"),
  CenterRight("center-right"),
  BottomLeft("bottom-left"),
  BottomCenter("bottom-center"),
  BottomRight("bottom-right"),
  ;

  override fun toString(): String = string
}

/**
 * [https://d2lang.com/tour/positions/#outside-and-border](https://d2lang.com/tour/positions/#outside-and-border)
 */
public enum class Location(override val string: String) : StringEnum {
  Border("border"),
  Inside("inside"),
  Outside("outside"),
  ;

  override fun toString(): String = string
}

/**
 * [https://d2lang.com/tour/style/#font](https://d2lang.com/tour/style/#font)
 */
public enum class Font(override val string: String) : StringEnum {
  Mono("mono"),
  ;

  override fun toString(): String = string
}

/**
 * [https://d2lang.com/tour/style/#text-transform](https://d2lang.com/tour/style/#text-transform)
 */
public enum class TextTransform(override val string: String) : StringEnum {
  Uppercase("uppercase"),
  Lowercase("lowercase"),
  Title("title"),
  None("none"),
  ;

  override fun toString(): String = string
}

/**
 * [https://eclipse.dev/elk/reference/algorithms.html](https://eclipse.dev/elk/reference/algorithms.html)
 */
public enum class ElkAlgorithm(override val string: String) : StringEnum {
  Box("box"),
  Disco("disco"),
  Fixed("fixed"),
  Force("force"),
  Layered("layered"),
  MrTree("mrtree"),
  Radial("radial"),
  Random("random"),
  RectPacking("rectpacking"),
  SporeCompaction("sporeCompaction"),
  SporeOverlap("sporeOverlap"),
  Stress("stress"),
  TopDownPacking("topdownpacking"),
  VertiFlex("vertiflex"),
  GraphvizCirco("graphviz.circo"),
  GraphvizDot("graphviz.dot"),
  GraphvizFdp("graphviz.fdp"),
  GraphvizNeato("graphviz.neato"),
  GraphvizTwopi("graphviz.twopi"),
  ;

  override fun toString(): String = string
}
