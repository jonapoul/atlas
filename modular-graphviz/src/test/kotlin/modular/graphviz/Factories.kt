/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz

import modular.core.internal.ModuleLink
import modular.core.internal.TypedModule
import modular.core.spec.Replacement
import modular.graphviz.internal.DotWriter

internal fun dotWriter(
  typedModules: Set<TypedModule> = emptySet(),
  links: Set<ModuleLink> = emptySet(),
  replacements: Set<Replacement> = emptySet(),
  thisPath: String = ":app",
  groupModules: Boolean = false,
  config: DotConfig = dotConfig(),
) = DotWriter(
  typedModules = typedModules,
  links = links,
  replacements = replacements,
  thisPath = thisPath,
  groupModules = groupModules,
  config = config,
)

fun dotConfig(
  arrowHead: String? = null,
  arrowTail: String? = null,
  dir: String? = null,
  dpi: Int? = null,
  fontSize: Int? = null,
  layoutEngine: String? = null,
  rankDir: String? = null,
  rankSep: Float? = null,
) = DotConfig(
  arrowHead = arrowHead,
  arrowTail = arrowTail,
  dir = dir,
  dpi = dpi,
  fontSize = fontSize,
  layoutEngine = layoutEngine,
  rankDir = rankDir,
  rankSep = rankSep,
)
