/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import modular.core.ModularDsl
import modular.core.PropertiesSpec

/**
 * See https://mermaid.js.org/config/theming.html#theme-variables for all custom properties, any of which can be set
 * with [put].
 */
@ModularDsl
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
