/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import modular.core.LinkType
import modular.core.ModuleType
import modular.core.internal.ModuleLink
import modular.core.internal.Node
import modular.core.internal.StringEnum
import modular.core.internal.TypedModule

internal fun node(
  path: String,
  type: ModuleType? = null,
) = Node(typedModule(path, type))

internal fun typedModule(
  path: String,
  type: ModuleType? = null,
) = TypedModule(
  projectPath = path,
  type = type,
)

internal fun moduleLink(
  fromPath: String,
  toPath: String,
  configuration: String = "implementation",
  style: StringEnum? = null,
  color: String? = null,
) = ModuleLink(
  fromPath,
  toPath,
  configuration,
  type = LinkType(configuration, style?.string, color),
)
