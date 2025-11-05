package atlas.d2

import atlas.core.PropertiesSpec

/**
 * A set of properties from https://d2lang.com/tour/style - applicable to both [D2ProjectTypeSpec] and [D2LinkTypeSpec].
 */
public interface D2PropertiesSpec : PropertiesSpec {
  public var animated: Boolean?
  public var bold: Boolean?
  public var borderRadius: Int?
  public var font: Font?
  public var fontColor: String?
  public var fontSize: Int?
  public var italic: Boolean?
  public var opacity: Float?
  public var stroke: String?
  public var strokeDash: Int?
  public var strokeWidth: Int?
  public var textTransform: TextTransform?
  public var underline: Boolean?
}
