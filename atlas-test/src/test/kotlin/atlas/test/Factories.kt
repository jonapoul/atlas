/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.test

import atlas.core.LinkType
import atlas.core.ModuleType
import atlas.core.internal.ModuleLink
import atlas.core.internal.Node
import atlas.core.internal.StringEnum
import atlas.core.internal.TypedModule

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
