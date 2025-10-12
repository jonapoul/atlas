/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.graphviz

import atlas.core.AtlasDsl
import atlas.core.AtlasExtension
import org.gradle.api.Action

@AtlasDsl
public interface GraphvizAtlasExtension : AtlasExtension {
  public val graphviz: GraphvizSpec
  public fun graphviz(action: Action<GraphvizSpec>)

  public fun linkTypes(action: Action<GraphvizNamedLinkTypeContainer>)
  public fun moduleTypes(action: Action<GraphvizNamedModuleTypeContainer>)
}
