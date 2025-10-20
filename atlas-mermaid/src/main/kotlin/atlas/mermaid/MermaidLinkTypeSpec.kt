package atlas.mermaid

import atlas.core.LinkTypeSpec
import atlas.core.PropertiesSpec

/**
 * https://mermaid.js.org/syntax/flowchart.html#links-between-nodes
 */
public interface MermaidLinkTypeSpec : LinkTypeSpec, PropertiesSpec {
  public var fontColor: String?
  public var stroke: String?
  public var strokeWidth: String?
  public var strokeDashArray: String?
}
