/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused", "SpellCheckingInspection") // public API

package modular.graphviz

import modular.core.internal.StringEnum
import modular.graphviz.LayoutEngine.Dot

/**
 * See https://graphviz.org/docs/attr-types/style/
 */
enum class LinkStyle(override val string: String) : StringEnum {
  Dashed("dashed"),
  Dotted("dotted"),
  Solid("solid"),
  Invis("invis"),
  Bold("bold"),
  Tapered("tapered"),
  ;

  override fun toString() = string
}

/**
 * See https://graphviz.org/docs/attr-types/style/
 */
enum class NodeStyle(override val string: String) : StringEnum {
  Dashed("dashed"),
  Dotted("dotted"),
  Solid("solid"),
  Invis("invis"),
  Bold("bold"),
  Filled("filled"),
  Striped("striped"),
  Wedged("wedged"),
  Diagonals("diagonals"),
  Rounded("rounded"),
  Radial("radial"),
  ;

  override fun toString() = string
}

/**
 * https://graphviz.org/docs/attr-types/rankdir/
 */
enum class RankDir(override val string: String) : StringEnum {
  TopToBottom("TB"),
  BottomToTop("BT"),
  LeftToRight("LR"),
  RightToLeft("RL"),
  ;

  override fun toString() = string
}

/**
 * https://graphviz.org/docs/attr-types/arrowType/
 */
enum class ArrowType(override val string: String) : StringEnum {
  Box("box"),
  Crow("crow"),
  Diamond("diamond"),
  Dot("dot"),
  Ediamond("ediamond"),
  Empty("empty"),
  HalfOpen("halfopen"),
  Inv("inv"),
  InvEmpty("invempty"),
  Invdot("invdot"),
  Invodot("invodot"),
  None("none"),
  Normal("normal"),
  Obox("obox"),
  Odiamond("odiamond"),
  Odot("odot"),
  Open("open"),
  Tee("tee"),
  Vee("vee"),
  ;

  override fun toString() = string
}

/**
 * https://graphviz.org/docs/layouts/
 *
 * Your machine might not have some of these, or it might have more! Use [GraphvizSpec.layoutEngine]
 * with a string parameter to configure a custom layout engine. [Dot] is the implicit default.
 *
 * Run `dot -v` and check under "layout" to see what you have locally.
 */
enum class LayoutEngine(override val string: String) : StringEnum {
  Dot("dot"),
  Neato("neato"),
  Fdp("fdp"),
  Sfdp("sfdp"),
  Circo("circo"),
  TwoPi("twopi"),
  Nop("nop"),
  Nop2("nop2"),
  Osage("osage"),
  Patchwork("patchwork"),
  ;

  override fun toString() = string
}

/**
 * https://graphviz.org/docs/attrs/dir/
 */
enum class Dir(override val string: String) : StringEnum {
  Forward("forward"),
  Back("back"),
  Both("both"),
  None("none"),
  ;

  override fun toString() = string
}

/**
 * https://graphviz.org/docs/attrs/imagepos/
 */
enum class ImagePos(override val string: String) : StringEnum {
  TopLeft("tl"),
  TopCenter("tc"),
  TopRight("tr"),
  MiddleLeft("ml"),
  MiddleCenter("mc"),
  MiddleRight("mr"),
  BottomLeft("bl"),
  BottomCenter("bc"),
  BottomRight("br"),
  ;

  override fun toString() = string
}

/**
 * https://graphviz.org/docs/attr-types/clusterMode/
 */
enum class ClusterMode(override val string: String) : StringEnum {
  Local("local"),
  Global("global"),
  None("none"),
  ;

  override fun toString() = string
}

/**
 * https://graphviz.org/docs/attr-types/smoothType/
 */
enum class SmoothType(override val string: String) : StringEnum {
  None("none"),
  AvgDist("avg_dist"),
  GraphDist("graph_dist"),
  PowerDist("power_dist"),
  Rng("rng"),
  Spring("spring"),
  Triangle("triangle"),
  ;

  override fun toString() = string
}

/**
 * The formats supported on your machine will depend on the version of Graphviz you use.
 * See https://graphviz.org/docs/outputs/
 */
enum class FileFormat(override val string: String) : StringEnum {
  Canon("canon"),
  Cmap("cmap"),
  Cmapx("cmapx"),
  CmapxNp("cmapx_np"),
  Dot("dot"),
  DotJson("dot_json"),
  Eps("eps"),
  Fig("fig"),
  Gd("gd"),
  Gd2("gd2"),
  Gif("gif"),
  Gv("gv"),
  Imap("imap"),
  ImapNp("imap_np"),
  Ismap("ismap"),
  Jpe("jpe"),
  Jpeg("jpeg"),
  Jpg("jpg"),
  Json("json"),
  Json0("json0"),
  Mp("mp"),
  Pdf("pdf"),
  Pic("pic"),
  Plain("plain"),
  PlainExt("plain-ext"),
  Png("png"),
  Pov("pov"),
  Ps("ps"),
  Ps2("ps2"),
  Svg("svg"),
  Svgz("svgz"),
  Tk("tk"),
  Vdx("vdx"),
  Vml("vml"),
  Vmlz("vmlz"),
  Vrml("vrml"),
  Wbmp("wbmp"),
  Webp("webp"),
  X11("x11"),
  Xdot("xdot"),
  Xdot12("xdot1.2"),
  Xdot14("xdot1.4"),
  XdotJson("xdot_json"),
  Xlib("xlib"),
  ;

  override fun toString() = string
}

/**
 * https://graphviz.org/doc/info/shapes.html
 */
enum class Shape(override val string: String) : StringEnum {
  Box("box"),
  Polygon("polygon"),
  Ellipse("ellipse"),
  Oval("oval"),
  Circle("circle"),
  Point("point"),
  Egg("egg"),
  Triangle("triangle"),
  Plaintext("plaintext"),
  Plain("plain"),
  Diamond("diamond"),
  Trapezium("trapezium"),
  Parallelogram("parallelogram"),
  House("house"),
  Pentagon("pentagon"),
  Hexagon("hexagon"),
  Septagon("septagon"),
  Octagon("octagon"),
  Doublecircle("doublecircle"),
  Doubleoctagon("doubleoctagon"),
  Tripleoctagon("tripleoctagon"),
  Invtriangle("invtriangle"),
  Invtrapezium("invtrapezium"),
  Invhouse("invhouse"),
  Mdiamond("mdiamond"),
  Msquare("msquare"),
  Mcircle("mcircle"),
  Rect("rect"),
  Rectangle("rectangle"),
  Square("square"),
  Star("star"),
  None("none"),
  Underline("underline"),
  Cylinder("cylinder"),
  Note("note"),
  Tab("tab"),
  Folder("folder"),
  Box3d("box3d"),
  Component("component"),
  Promoter("promoter"),
  Cds("cds"),
  Terminator("terminator"),
  Utr("utr"),
  Primersite("primersite"),
  Restrictionsite("restrictionsite"),
  Fivepoverhang("fivepoverhang"),
  Threepoverhang("threepoverhang"),
  Noverhang("noverhang"),
  Assembly("assembly"),
  Signature("signature"),
  Insulator("insulator"),
  Ribosite("ribosite"),
  Rnastab("rnastab"),
  Proteasesite("proteasesite"),
  Proteinstab("proteinstab"),
  Rpromoter("rpromoter"),
  Rarrow("rarrow"),
  Larrow("larrow"),
  Lpromoter("lpromoter"),
  ;

  override fun toString() = string
}
