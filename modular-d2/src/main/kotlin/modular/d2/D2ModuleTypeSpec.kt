package modular.d2

import modular.core.ModuleTypeSpec

/**
 * Style specs from https://d2lang.com/tour/style, applied to the shape matching this module type.
 */
interface D2ModuleTypeSpec : ModuleTypeSpec {
  var animated: Boolean?
  var bold: Boolean?
  var borderRadius: Int?
  var doubleBorder: String?
  var fillPattern: FillPattern?
  var font: Font?
  var fontColor: String?
  var fontSize: Int?
  var italic: Boolean?
  var multiple: Boolean?
  var opacity: Float?
  var render3D: Boolean?
  var shadow: Boolean?
  var stroke: String?
  var strokeDash: Int?
  var strokeWidth: Int?
  var textTransform: TextTransform?
  var underline: Boolean?
}
