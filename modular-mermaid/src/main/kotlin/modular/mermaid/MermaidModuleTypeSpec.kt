/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import modular.core.ModuleTypeSpec

/**
 * From https://mermaid.js.org/syntax/flowchart.html#styling-a-node. Modular won't validate any of these values, you
 * need to make sure they're in the right format. E.g. [strokeDashArray] as an array of integers ("5 5"), [strokeWidth]
 * as a pixel size ("5px"). See the above link for the expected formats.
 *
 * Use [put] to insert any custom properties, but the below ones are all the available ones (AFAIK) as of writing this.
 */
interface MermaidModuleTypeSpec : ModuleTypeSpec {
  var fontColor: String?
  var fontSize: String?
  var stroke: String?
  var strokeDashArray: String?
  var strokeWidth: String?
}
