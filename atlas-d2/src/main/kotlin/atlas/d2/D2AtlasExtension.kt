package atlas.d2

import atlas.core.AtlasDsl
import atlas.core.AtlasExtension
import org.gradle.api.Action

@AtlasDsl
public interface D2AtlasExtension : AtlasExtension {
  public val d2: D2Spec
  public fun d2(action: Action<D2Spec>)

  /**
   * Configure [D2LinkTypeSpec]s using D2-specific [LinkStyle]s and any D2-specific attributes.
   */
  public fun linkTypes(action: Action<D2NamedLinkTypeContainer>)

  /**
   * Configure [D2ProjectTypeSpec]s using D2-specific attributes.
   */
  public fun projectTypes(action: Action<D2NamedProjectTypeContainer>)
}
