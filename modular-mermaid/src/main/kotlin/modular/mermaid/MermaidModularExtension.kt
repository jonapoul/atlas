/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import modular.core.ModularDsl
import modular.core.ModularExtension
import org.gradle.api.Action

interface MermaidModularExtension : ModularExtension {
  val mermaid: MermaidSpec
  @ModularDsl fun mermaid(action: Action<MermaidSpec>): MermaidSpec
}
