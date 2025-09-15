/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.internal.ModuleLink
import modular.internal.Replacement
import modular.internal.TypedModule
import modular.internal.buildIndentedString
import modular.mermaid.spec.MermaidConfig

internal class MermaidWriter(
  private val typedModules: Set<TypedModule>,
  private val links: Set<ModuleLink>,
  private val replacements: MutableSet<Replacement>,
  private val thisPath: String,
  private val groupModules: Boolean,
  private val config: MermaidConfig,
) {
  operator fun invoke(): String = buildIndentedString(size = 2) {
    appendLine("TBC")
  }
}
