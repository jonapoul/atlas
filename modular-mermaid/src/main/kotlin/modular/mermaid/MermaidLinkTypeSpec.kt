/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import modular.core.LinkTypeSpec
import modular.core.PropertiesSpec

/**
 * https://mermaid.js.org/syntax/flowchart.html#links-between-nodes
 */
public interface MermaidLinkTypeSpec : LinkTypeSpec, PropertiesSpec {
  public var stroke: String?
  public var strokeWidth: String?
  public var strokeDashArray: String?
}
