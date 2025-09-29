/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz

import modular.core.ModularDsl
import modular.core.ModularExtension
import org.gradle.api.Action

interface GraphvizModularExtension : ModularExtension {
  val graphviz: GraphvizSpec
  @ModularDsl fun graphviz(action: Action<GraphvizSpec>): GraphvizSpec
}
