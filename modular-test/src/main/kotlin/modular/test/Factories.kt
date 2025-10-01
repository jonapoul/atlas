/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import modular.core.internal.ModuleLink
import modular.core.internal.Node
import modular.core.internal.TypedModule
import modular.core.spec.ModuleType

fun node(path: String) = Node(typedModule(path))

fun typedModule(
  path: String,
  type: ModuleType? = null,
) = TypedModule(
  projectPath = path,
  type = type,
)

fun moduleLink(
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
