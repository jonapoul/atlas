package atlas.d2

import atlas.core.ModuleTypeSpec

/**
 * Style specs from https://d2lang.com/tour/style, applied to the shape matching this module type.
 *
 * You can use any of the below properties, plus those in [D2PropertiesSpec] shared between links and modules.
 */
public interface D2ModuleTypeSpec : ModuleTypeSpec, D2PropertiesSpec {
  /**
   * Only applicable to [Shape.Rectangle] and [Shape.Oval]
   */
  public var doubleBorder: String?

  public var fill: String?

  public var fillPattern: FillPattern?

  public var multiple: Boolean?

  /**
   * Only applicable to [Shape.Rectangle] and [Shape.Square]
   */
  public var render3D: Boolean?

  public var shadow: Boolean?

  public var shape: Shape?
}
