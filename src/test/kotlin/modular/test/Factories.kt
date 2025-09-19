/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import modular.core.internal.ModuleLink
import modular.core.internal.Node
import modular.core.internal.Replacement
import modular.core.internal.TypedModule
import modular.core.spec.ModuleTypeModel
import modular.graphviz.internal.DotWriter
import modular.graphviz.spec.DotConfig

internal fun node(path: String) = Node(typedModule(path))

internal fun typedModule(
  path: String,
  type: ModuleTypeModel? = null,
) = TypedModule(
  projectPath = path,
  type = type,
)

internal fun moduleLink(
  fromPath: String,
  toPath: String,
  configuration: String = "implementation",
  style: String? = null,
  color: String? = null,
) = ModuleLink(
  fromPath,
  toPath,
  configuration,
  style,
  color,
)

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

internal fun dotConfig(
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
