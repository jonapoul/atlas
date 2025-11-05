package atlas.graphviz

import atlas.core.AtlasDsl
import atlas.core.AtlasExtension
import org.gradle.api.Action

@AtlasDsl
public interface GraphvizAtlasExtension : AtlasExtension {
  /**
   * Perform Graphviz-specific configuration of the project diagrams.
   */
  public val graphviz: GraphvizSpec
  public fun graphviz(action: Action<GraphvizSpec>)

  /**
   * Configure [atlas.core.LinkTypeSpec]s using Graphviz-specific [LinkStyle]s and any Graphviz-specific attributes.
   */
  public fun linkTypes(action: Action<GraphvizNamedLinkTypeContainer>)

  /**
   * Configure [atlas.core.ProjectTypeSpec]s using Graphviz-specific attributes.
   */
  public fun projectTypes(action: Action<GraphvizNamedProjectTypeContainer>)
}
