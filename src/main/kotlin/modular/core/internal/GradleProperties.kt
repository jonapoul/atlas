/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import org.gradle.api.Project
import org.gradle.api.provider.Provider

/**
 * Just to keep all gradle property references together - easier to document
 */
internal class GradleProperties(private val project: Project) {
  // General
  val alsoTraverseUpwards: Provider<Boolean> = bool("modular.alsoTraverseUpwards", default = false)
  val autoApplyToChildren: Provider<Boolean> = bool("modular.autoApplyToChildren", default = false)
  val generateOnSync: Provider<Boolean> = bool(key = "modular.generateOnSync", default = false)
  val groupModules: Provider<Boolean> = bool(key = "modular.groupModules", default = false)
  val separator: Provider<String> = string(key = "modular.separator", default = ",")
  val printFilesToConsole: Provider<Boolean> = bool(key = "modular.printFilesToConsole", default = false)

  // GraphViz
  val adjustSvgViewBox: Provider<Boolean> = bool(key = "modular.graphviz.adjustSvgViewBox", default = false)
  val gvWriteReadme: Provider<Boolean> = bool(key = "modular.graphviz.writeReadme", default = false)
  val arrowHead: Provider<String> = string(key = "modular.graphViz.chart.arrowHead", default = null)
  val arrowTail: Provider<String> = string(key = "modular.graphViz.chart.arrowTail", default = null)
  val dir: Provider<String> = string(key = "modular.graphViz.chart.dir", default = null)
  val dpi: Provider<Int> = int(key = "modular.graphViz.chart.dpi", default = null)
  val fileFormat: Provider<String> = string(key = "modular.graphViz.chart.fileFormat", default = "svg")
  val fontSize: Provider<Int> = int(key = "modular.graphViz.chart.fontSize", default = null)
  val layoutEngine: Provider<String> = string(key = "modular.graphViz.chart.layoutEngine", default = null)
  val rankDir: Provider<String> = string(key = "modular.graphViz.chart.rankDir", default = null)
  val rankSep: Provider<Float> = float(key = "modular.graphViz.chart.rankSep", default = null)

  // Mermaid chart
  val mermaidLook: Provider<String> = string(key = "modular.mermaid.chart.look", default = null)
  val mermaidTheme: Provider<String> = string(key = "modular.mermaid.chart.theme", default = null)
  val mermaidAnimateLinks: Provider<Boolean> = bool(key = "modular.mermaid.chart.animateLinks", default = false)
  val mermaidWriteReadme: Provider<Boolean> = bool(key = "modular.mermaid.writeReadme", default = false)

  // Suppressions
  val suppressSvgViewBoxWarning: Provider<Boolean> = bool(key = "modular.suppress.adjustSvgViewBox", default = false)
  val suppressNoGraphVizOutputs: Provider<Boolean> = bool(key = "modular.suppress.noGraphVizOutputs", default = false)

  private fun bool(key: String, default: Boolean? = null) = prop(key, default, mapper = String::toBooleanStrict)
  private fun float(key: String, default: Float? = null) = prop(key, default, mapper = String::toFloat)
  private fun int(key: String, default: Int? = null) = prop(key, default, mapper = String::toInt)
  private fun string(key: String, default: String? = null) = prop(key, default, mapper = { it })

  private inline fun <reified T : Any> prop(key: String, default: T?, noinline mapper: (String) -> T?) =
    project.providers
      .gradleProperty(key)
      .map(mapper)
      .orElse(project.provider { default })
}
