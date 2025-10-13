/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.d2

import atlas.core.AtlasDsl
import atlas.core.PropertiesSpec
import org.gradle.api.Action
import org.gradle.api.provider.Property

@AtlasDsl
public interface D2LayoutEngineSpec : PropertiesSpec {
  public val layoutEngine: Property<LayoutEngine>

  public val elk: D2ElkSpec
  public fun elk(config: Action<D2ElkSpec>? = null)

  public val dagre: D2DagreSpec
  public fun dagre(config: Action<D2DagreSpec>? = null)

  public val tala: D2TalaSpec
  public fun tala(config: Action<D2TalaSpec>? = null)
}

/**
 * From running `d2 layout elk` in the CLI
 */
@AtlasDsl
public interface D2ElkSpec : PropertiesSpec {
  /**
   * layout algorithm (default "layered")
   */
  public var algorithm: ElkAlgorithm?

  /**
   * the spacing to be preserved between nodes and edges that are routed next to the node’s layer (default 40)
   */
  public var edgeNodeBetweenLayers: Int?

  /**
   * the spacing to be preserved between any pair of nodes of two adjacent layers (default 70)
   */
  public var nodeNodeBetweenLayers: Int?

  /**
   * spacing to be preserved between a node and its self loops (default 50)
   */
  public var nodeSelfLoop: Int?

  /**
   * the padding to be left to a parent element’s border when placing child elements
   * (default "[top=50,left=50,bottom=50,right=50]")
   */
  public var padding: String?
}

/**
 * From running `d2 layout dagre` in the CLI
 */
@AtlasDsl
public interface D2DagreSpec : PropertiesSpec {
  /**
   * number of pixels that separate nodes horizontally. (default 60)
   */
  public var nodeSep: Int?

  /**
   * number of pixels that separate edges horizontally. (default 20)
   */
  public var edgeSep: Int?
}

@AtlasDsl
public interface D2TalaSpec : PropertiesSpec {
  // TBC?
}
