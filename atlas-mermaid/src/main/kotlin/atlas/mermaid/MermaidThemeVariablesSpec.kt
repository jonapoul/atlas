/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.mermaid

import atlas.core.AtlasDsl
import atlas.core.PropertiesSpec

/**
 * Global diagram configuration properties.
 *
 * See [the Mermaid docs](https://mermaid.js.org/config/theming.html#theme-variables)
 */
@AtlasDsl
public interface MermaidThemeVariablesSpec : PropertiesSpec {
  public var background: String?
  public var darkMode: Boolean?
  public var fontFamily: String?
  public var fontSize: String?
  public var lineColor: String?
  public var primaryBorderColor: String?
  public var primaryColor: String?
  public var primaryTextColor: String?
  public var secondaryColor: String?
  public var tertiaryColor: String?
}
