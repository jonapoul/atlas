/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import modular.spec.RankDir
import org.gradle.api.Project
import org.gradle.api.provider.Provider

/**
 * Just to keep all gradle property references together - easier to document
 */
internal class ModularProperties(private val project: Project) {
  // General settings
  val generateOnSync: Provider<Boolean> = bool(key = "modular.generateOnSync", default = false)
  val generateReadme: Provider<Boolean> = bool(key = "modular.generateReadme", default = false)
  val separator: Provider<String> = string(key = "modular.separator", default = ",")
  val supportUpwardsTraversal: Provider<Boolean> = bool(key = "modular.supportUpwardsTraversal", default = false)

  // Experimental
  val adjustSvgViewBox: Provider<Boolean> = bool(key = "modular.experimental.adjustSvgViewBox", default = false)

  // Dotfile legend
  val cellBorder: Provider<Int> = int(key = "modular.dotfile.legend.cellBorder", default = 1)
  val cellPadding: Provider<Int> = int(key = "modular.dotfile.legend.cellPadding", default = 4)
  val cellSpacing: Provider<Int> = int(key = "modular.dotfile.legend.cellSpacing", default = 0)
  val tableBorder: Provider<Int> = int(key = "modular.dotfile.legend.tableBorder", default = 0)

  // Dotfile chart
  val arrowHead: Provider<String> = string(key = "modular.dotfile.chart.arrowHead", default = null)
  val arrowTail: Provider<String> = string(key = "modular.dotfile.chart.arrowTail", default = null)
  val dpi: Provider<Int> = int(key = "modular.dotfile.chart.dpi", default = null)
  val layoutEngine: Provider<String> = string(key = "modular.dotfile.chart.layoutEngine", default = null)
  val fontSize: Provider<Int> = int(key = "modular.dotfile.chart.fontSize", default = null)
  val rankDir: Provider<RankDir> = enum(key = "modular.dotfile.chart.rankDir", default = RankDir.TopToBottom)
  val rankSep: Provider<Float> = float(key = "modular.dotfile.chart.rankSep", default = null)
  val showArrows: Provider<Boolean> = bool(key = "modular.dotfile.chart.showArrows", default = true)

  private fun bool(key: String, default: Boolean? = null) = prop(key, default, mapper = String::toBooleanStrict)
  private fun float(key: String, default: Float? = null) = prop(key, default, mapper = String::toFloat)
  private fun int(key: String, default: Int? = null) = prop(key, default, mapper = String::toInt)
  private fun string(key: String, default: String? = null) = prop(key, default, mapper = { it })

  private inline fun <reified E : Enum<E>> enum(key: String, default: E? = null) =
    prop(key, default) { value -> enumValues<E>().firstOrNull { it.toString() == value } ?: default }

  private inline fun <reified T : Any> prop(key: String, default: T?, noinline mapper: (String) -> T?) =
    project.providers
      .gradleProperty(key)
      .map(mapper)
      .orElse(project.provider { default })
}
