/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.mermaid

import atlas.core.AtlasDsl
import atlas.core.AtlasExtension
import org.gradle.api.Action

@AtlasDsl
public interface MermaidAtlasExtension : AtlasExtension {
  public val mermaid: MermaidSpec
  public fun mermaid(action: Action<MermaidSpec>)

  public fun linkTypes(action: Action<MermaidNamedLinkTypeContainer>)
  public fun moduleTypes(action: Action<MermaidNamedModuleTypeContainer>)
}
