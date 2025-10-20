package atlas.d2

import atlas.core.AtlasDsl
import atlas.core.PropertiesSpec
import org.gradle.api.Action
import org.gradle.api.provider.Property

@AtlasDsl
public interface D2LayoutEngineSpec : PropertiesSpec {
  public val layoutEngine: Property<LayoutEngine>

  /**
   * Configure ELK layout engine for the output chart.
   */
  public val elk: D2ElkSpec
  public fun elk(config: Action<D2ElkSpec>? = null)

  /**
   * Configure DAGRE layout engine for the output chart. This is the default engine.
   */
  public val dagre: D2DagreSpec
  public fun dagre(config: Action<D2DagreSpec>? = null)

  /**
   * Configure TALA layout engine for the output chart. This is probably not installed on your machine, but if it is
   * you can call [D2TalaSpec.put] to apply custom config properties.
   */
  public val tala: D2TalaSpec
  public fun tala(config: Action<D2TalaSpec>? = null)
}

/**
 * CLI configuration options, found from running `d2 layout elk` in the CLI.
 */
@AtlasDsl
public interface D2ElkSpec : PropertiesSpec {
  /**
   * Layout algorithm (default [ElkAlgorithm.Layered])
   */
  public var algorithm: ElkAlgorithm?

  /**
   * The spacing to be preserved between nodes and edges that are routed next to the node’s layer (default 40)
   */
  public var edgeNodeBetweenLayers: Int?

  /**
   * The spacing to be preserved between any pair of nodes of two adjacent layers (default 70)
   */
  public var nodeNodeBetweenLayers: Int?

  /**
   * Spacing to be preserved between a node and its self loops (default 50)
   */
  public var nodeSelfLoop: Int?

  /**
   * The padding to be left to a parent element’s border when placing child elements
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
   * Number of pixels that separate nodes horizontally. (default 60)
   */
  public var nodeSep: Int?

  /**
   * Number of pixels that separate edges horizontally. (default 20)
   */
  public var edgeSep: Int?
}

/**
 * Not installed in the default D2 installation so this won't normally work, unless you have a custom build of D2.
 */
@AtlasDsl
public interface D2TalaSpec : PropertiesSpec {
  // TBC?
}
