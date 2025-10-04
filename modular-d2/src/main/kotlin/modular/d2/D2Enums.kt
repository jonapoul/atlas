/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.d2

import modular.core.internal.StringEnum

/**
 * https://d2lang.com/tour/layouts/#direction
 */
enum class Direction(override val string: String) : StringEnum {
  Up("up"),
  Down("down"),
  Right("right"),
  Left("left"),
  ;

  override fun toString() = string
}

/**
 * https://d2lang.com/tour/style/#fill-pattern
 */
enum class FillPattern(override val string: String) : StringEnum {
  Dots("dots"),
  Lines("lines"),
  Grain("grain"),
  None("none"),
  ;

  override fun toString() = string
}

/**
 * https://d2lang.com/tour/connections/#arrowheads
 */
enum class ArrowType(override val string: String) : StringEnum {
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

  override fun toString() = string
}
