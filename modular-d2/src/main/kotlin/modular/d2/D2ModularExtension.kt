/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import modular.core.ModularDsl
import modular.core.ModularExtension
import org.gradle.api.Action

@ModularDsl
interface D2ModularExtension : ModularExtension {
  val d2: D2Spec
  fun d2(action: Action<D2Spec>): D2Spec

  fun linkTypes(action: Action<D2NamedLinkTypeContainer>)
  fun moduleTypes(action: Action<D2NamedModuleTypeContainer>)
}
