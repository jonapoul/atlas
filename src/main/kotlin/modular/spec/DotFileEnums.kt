/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.spec

/**
 * https://graphviz.org/docs/attr-types/rankdir/
 */
enum class RankDir(private val string: String) {
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
enum class ArrowType(private val string: String) {
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
