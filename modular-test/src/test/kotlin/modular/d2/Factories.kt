/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import modular.core.internal.ModuleLink
import modular.core.internal.TypedModule
import modular.core.spec.Replacement
import modular.d2.internal.D2Writer

internal fun d2Writer(
  typedModules: Set<TypedModule> = emptySet(),
  links: Set<ModuleLink> = emptySet(),
  replacements: Set<Replacement> = emptySet(),
  thisPath: String = ":app",
  groupModules: Boolean = false,
  config: D2Config = d2Config(),
) = D2Writer(
  typedModules = typedModules,
  links = links,
  replacements = replacements,
  thisPath = thisPath,
  groupModules = groupModules,
  config = config,
)

fun d2Config(
  containerLabelPosition: String? = null,
  style: Map<String, String>? = null,
) = D2Config(
  containerLabelPosition = containerLabelPosition,
  style = style,
)
