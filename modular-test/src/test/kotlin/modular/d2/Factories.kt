/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import modular.core.Replacement
import modular.d2.internal.D2Writer
import modular.test.ProjectLayout

internal fun d2Writer(
  layout: ProjectLayout,
  replacements: Set<Replacement> = emptySet(),
  thisPath: String = ":app",
  groupModules: Boolean = false,
  classesRelativePath: String = "../classes.d2",
) = D2Writer(
  typedModules = layout.modules,
  links = layout.links,
  replacements = replacements,
  thisPath = thisPath,
  groupModules = groupModules,
  globalRelativePath = classesRelativePath,
)
