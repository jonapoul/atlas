/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz

import modular.core.ModularDsl
import modular.core.ModularExtension
import org.gradle.api.Action

@ModularDsl
public interface GraphvizModularExtension : ModularExtension {
  public val graphviz: GraphvizSpec
  public fun graphviz(action: Action<GraphvizSpec>)

  public fun linkTypes(action: Action<GraphvizNamedLinkTypeContainer>)
  public fun moduleTypes(action: Action<GraphvizNamedModuleTypeContainer>)
}
