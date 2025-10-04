/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import modular.core.ModularDsl
import modular.core.internal.PropertiesSpec

/**
 * See https://mermaid.js.org/config/theming.html#theme-variables for all custom properties, any of which can be set
 * with [put].
 */
@ModularDsl
interface MermaidThemeVariablesSpec : PropertiesSpec {
  var background: String?
  var darkMode: Boolean?
  var fontFamily: String?
  var fontSize: String?
  var lineColor: String?
  var primaryBorderColor: String?
  var primaryColor: String?
  var primaryTextColor: String?
  var secondaryColor: String?
  var tertiaryColor: String?
}
