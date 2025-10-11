/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import modular.core.ModularDsl
import modular.core.ModularExtension
import org.gradle.api.Action

@ModularDsl
public interface MermaidModularExtension : ModularExtension {
  public val mermaid: MermaidSpec
  public fun mermaid(action: Action<MermaidSpec>)

  public fun linkTypes(action: Action<MermaidNamedLinkTypeContainer>)
  public fun moduleTypes(action: Action<MermaidNamedModuleTypeContainer>)
}
