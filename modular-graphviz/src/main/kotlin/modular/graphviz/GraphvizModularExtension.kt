/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz

import modular.core.ModularDsl
import modular.core.ModularExtension
import org.gradle.api.Action

@ModularDsl
interface GraphvizModularExtension : ModularExtension {
  val graphviz: GraphvizSpec
  fun graphviz(action: Action<GraphvizSpec>): GraphvizSpec

  fun linkTypes(action: Action<GraphvizNamedLinkTypeContainer>)
}
