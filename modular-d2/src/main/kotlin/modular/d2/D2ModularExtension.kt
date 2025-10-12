/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import modular.core.ModularDsl
import modular.core.ModularExtension
import org.gradle.api.Action

@ModularDsl
public interface D2ModularExtension : ModularExtension {
  public val d2: D2Spec
  public fun d2(action: Action<D2Spec>)

  public fun linkTypes(action: Action<D2NamedLinkTypeContainer>)
  public fun moduleTypes(action: Action<D2NamedModuleTypeContainer>)
}
