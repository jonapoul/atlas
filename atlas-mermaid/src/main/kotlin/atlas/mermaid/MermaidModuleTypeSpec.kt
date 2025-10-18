/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.mermaid

import atlas.core.ModuleTypeSpec

/**
 * From [https://mermaid.js.org/syntax/flowchart.html#styling-a-node](https://mermaid.js.org/syntax/flowchart.html#styling-a-node).
 * Atlas won't validate any of these values, so you need to make sure they're in the right format. E.g.
 * [strokeDashArray] as an array of integers like `"5 5"`, [strokeWidth] as a pixel size ("5px") or an integer ("5").
 * See the above link for the expected formats.
 *
 * Use [put] to insert any custom properties, but the below ones are all the available ones (AFAIK) as of writing this.
 */
public interface MermaidModuleTypeSpec : ModuleTypeSpec {
  public var fontColor: String?
  public var fontSize: String?
  public var stroke: String?
  public var strokeDashArray: String?
  public var strokeWidth: String?
}
