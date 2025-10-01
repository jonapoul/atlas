/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import modular.core.ModularDsl
import org.gradle.api.provider.MapProperty

/**
 * See https://mermaid.js.org/config/theming.html#theme-variables for all custom properties, any of which can be set
 * with [put].
 */
@ModularDsl
interface MermaidThemeVariablesSpec {
  val properties: MapProperty<String, String>
  fun put(key: String, value: Any) = properties.put(key, value.toString())

  fun background(value: String)
  fun darkMode(value: Boolean)
  fun fontFamily(value: String)
  fun fontSize(value: String)
  fun lineColor(value: String)
  fun primaryBorderColor(value: String)
  fun primaryColor(value: String)
  fun primaryTextColor(value: String)
  fun secondaryColor(value: String)
  fun tertiaryColor(value: String)
}
