/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import modular.graphviz.internal.DotFileWriter
import modular.graphviz.spec.DotFileConfig
import modular.internal.ModuleLink
import modular.internal.Node
import modular.internal.Replacement
import modular.internal.TypedModule
import modular.spec.LinkType
import modular.spec.ModuleTypeModel

internal fun node(path: String) = Node(typedModule(path))

internal fun typedModule(
  path: String,
  type: ModuleTypeModel? = null,
) = TypedModule(
  projectPath = path,
  type = type,
  separator = ",",
)

internal fun moduleLink(
  fromPath: String,
  toPath: String,
  configuration: String = "implementation",
) = ModuleLink(
  fromPath,
  toPath,
  configuration,
  separator = ",",
)

internal fun dotFileWriter(
  typedModules: Set<TypedModule> = emptySet(),
  links: Set<ModuleLink> = emptySet(),
  linkTypes: Set<LinkType> = emptySet(),
  replacements: Set<Replacement> = emptySet(),
  thisPath: String = ":app",
  groupModules: Boolean = false,
  config: DotFileConfig = dotFileConfig(),
) = DotFileWriter(
  typedModules = typedModules,
  links = links,
  linkTypes = linkTypes,
  replacements = replacements,
  thisPath = thisPath,
  groupModules = groupModules,
  config = config,
)

internal fun dotFileConfig(
  arrowHead: String? = null,
  arrowTail: String? = null,
  dir: String? = null,
  dpi: Int? = null,
  fontSize: Int? = null,
  layoutEngine: String? = null,
  rankDir: String? = null,
  rankSep: Float? = null,
) = DotFileConfig(
  arrowHead = arrowHead,
  arrowTail = arrowTail,
  dir = dir,
  dpi = dpi,
  fontSize = fontSize,
  layoutEngine = layoutEngine,
  rankDir = rankDir,
  rankSep = rankSep,
)
