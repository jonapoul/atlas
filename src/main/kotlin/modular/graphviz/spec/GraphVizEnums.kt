/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.graphviz.spec

import modular.core.spec.Style

interface StringEnum {
  val string: String
}

/**
 * https://graphviz.org/docs/attr-types/rankdir/
 */
enum class RankDir(override val string: String) : StringEnum {
  TopToBottom("TB"),
  BottomToTop("BT"),
  LeftToRight("LR"),
  RightToLeft("RL"),
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
}

/**
 * https://graphviz.org/docs/layouts/
 *
 * Your machine might not have some of these, or it might have more! Use [GraphVizChartSpec.layoutEngine]
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
}

/**
 * https://graphviz.org/docs/attrs/dir/
 */
enum class Dir(override val string: String) : StringEnum {
  Forward("forward"),
  Back("back"),
  Both("both"),
  None("none"),
}

/**
 * https://graphviz.org/docs/attr-types/style/
 */
enum class LinkStyle(override val string: String) : Style {
  Dashed("dashed"),
  Dotted("dotted"),
  Solid("solid"),
  Invis("invis"),
  Bold("bold"),
}
