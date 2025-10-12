/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import modular.core.ModuleTypeSpec

/**
 * Style specs from https://d2lang.com/tour/style, applied to the shape matching this module type.
 */
public interface D2ModuleTypeSpec : ModuleTypeSpec {
  public var animated: Boolean?
  public var bold: Boolean?
  public var borderRadius: Int?
  public var doubleBorder: String?
  public var fillPattern: FillPattern?
  public var font: Font?
  public var fontColor: String?
  public var fontSize: Int?
  public var italic: Boolean?
  public var multiple: Boolean?
  public var opacity: Float?
  public var render3D: Boolean?
  public var shadow: Boolean?
  public var shape: Shape?
  public var stroke: String?
  public var strokeDash: Int?
  public var strokeWidth: Int?
  public var textTransform: TextTransform?
  public var underline: Boolean?
}
