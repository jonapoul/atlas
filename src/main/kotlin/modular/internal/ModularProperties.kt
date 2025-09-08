/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import org.gradle.api.Project
import org.gradle.api.provider.Provider

/**
 * Just to keep all gradle property references together - easier to document
 */
internal class ModularProperties(private val project: Project) {
  // General settings
  val applyToSubprojects: Provider<Boolean> = bool(key = "modular.applyToSubprojects", default = true)
  val generateOnSync: Provider<Boolean> = bool(key = "modular.generateOnSync", default = false)
  val generateReadme: Provider<Boolean> = bool(key = "modular.generateReadme", default = false)
  val removeModulePrefix: Provider<String> = string(key = "modular.removeModulePrefix", default = "")
  val separator: Provider<String> = string(key = "modular.separator", default = ",")
  val supportUpwardsTraversal: Provider<Boolean> = bool(key = "modular.supportUpwardsTraversal", default = false)

  // Dotfile legend
  val tableBorder: Provider<Int> = int(key = "modular.dotfile.legend.tableBorder", default = 0)
  val cellBorder: Provider<Int> = int(key = "modular.dotfile.legend.cellBorder", default = 1)
  val cellSpacing: Provider<Int> = int(key = "modular.dotfile.legend.cellSpacing", default = 0)
  val cellPadding: Provider<Int> = int(key = "modular.dotfile.legend.cellPadding", default = 4)

  private fun bool(key: String, default: Boolean) =
    project.providers.gradleProperty(key).map { it.toBooleanStrict() }.orElse(default)

  private fun int(key: String, default: Int) =
    project.providers.gradleProperty(key).map { it.toInt() }.orElse(default)

  private fun string(key: String, default: String) =
    project.providers.gradleProperty(key).orElse(default)
}
