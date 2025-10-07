/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused", "MagicNumber") // public API

package modular.d2

import modular.core.internal.IntEnum
import modular.core.internal.StringEnum

/**
 * https://d2lang.com/tour/layouts/
 */
enum class LayoutEngine(override val string: String) : StringEnum {
  Dagre("dagre"), // https://d2lang.com/tour/dagre/
  Elk("elk"), // https://d2lang.com/tour/elk/
  Tala("tala"), // https://d2lang.com/tour/tala/
  ;

  override fun toString() = string
}

/**
 * https://d2lang.com/tour/themes/
 */
enum class Theme(override val value: Int) : IntEnum {
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
 * https://d2lang.com/tour/exports/
 */
enum class FileFormat(override val string: String) : StringEnum {
  Svg("svg"),
  Png("png"),
  Pdf("pdf"),
  Pptx("pptx"),
  Gif("gif"),
  Ascii("txt"),
  ;

  override fun toString() = string
}

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

/**
 * https://d2lang.com/tour/positions/
 */
enum class Position(override val string: String) : StringEnum {
  TopLeft("top-left"),
  TopCenter("top-center"),
  TopRight("top-right"),
  CenterLeft("center-left"),
  CenterRight("center-right"),
  BottomLeft("bottom-left"),
  BottomCenter("bottom-center"),
  BottomRight("bottom-right"),
  ;

  override fun toString() = string
}

/**
 * https://d2lang.com/tour/positions/#outside-and-border
 */
enum class Location(override val string: String) : StringEnum {
  Border("border"),
  Inside("inside"),
  Outside("outside"),
  ;

  override fun toString() = string
}

/**
 * https://d2lang.com/tour/style/#font
 */
enum class Font(override val string: String) : StringEnum {
  Mono("mono"),
  ;

  override fun toString() = string
}
