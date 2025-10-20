package atlas.mermaid

import atlas.core.AtlasDsl
import atlas.core.AtlasExtension
import org.gradle.api.Action

@AtlasDsl
public interface MermaidAtlasExtension : AtlasExtension {
  /**
   * Perform Mermaid-specific configuration of the module diagrams.
   */
  public val mermaid: MermaidSpec
  public fun mermaid(action: Action<MermaidSpec>)

  /**
   * Configure [MermaidLinkTypeSpec]s using Mermaid-specific [LinkStyle]s and any Mermaid-specific attributes.
   */
  public fun linkTypes(action: Action<MermaidNamedLinkTypeContainer>)

  /**
   * Configure [MermaidModuleTypeSpec]s using Mermaid-specific attributes.
   */
  public fun moduleTypes(action: Action<MermaidNamedModuleTypeContainer>)
}
