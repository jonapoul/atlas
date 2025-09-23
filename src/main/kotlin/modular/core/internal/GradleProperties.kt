/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class GradleProperties(project: Project) {
  val general = GeneralGradleProperties(project)
  val graphviz = GraphvizGradleProperties(project)
  val mermaid = MermaidGradleProperties(project)
}

internal class GeneralGradleProperties(override val project: Project) : IGradleProperties {
  val alsoTraverseUpwards: Provider<Boolean> = bool("modular.alsoTraverseUpwards", default = false)
  val autoApplyToChildren: Provider<Boolean> = bool("modular.autoApplyToChildren", default = false)
  val generateOnSync: Provider<Boolean> = bool(key = "modular.generateOnSync", default = false)
  val groupModules: Provider<Boolean> = bool(key = "modular.groupModules", default = false)
  val printFilesToConsole: Provider<Boolean> = bool(key = "modular.printFilesToConsole", default = false)
  val separator: Provider<String> = string(key = "modular.separator", default = ",")
}

internal class GraphvizGradleProperties(override val project: Project) : IGradleProperties {
  val adjustSvgViewBox: Provider<Boolean> = bool(key = "modular.graphviz.adjustSvgViewBox", default = false)
  val arrowHead: Provider<String> = string(key = "modular.graphviz.chart.arrowHead", default = null)
  val arrowTail: Provider<String> = string(key = "modular.graphviz.chart.arrowTail", default = null)
  val dir: Provider<String> = string(key = "modular.graphviz.chart.dir", default = null)
  val dpi: Provider<Int> = int(key = "modular.graphviz.chart.dpi", default = null)
  val fileFormat: Provider<String> = string(key = "modular.graphviz.chart.fileFormat", default = "svg")
  val fontSize: Provider<Int> = int(key = "modular.graphviz.chart.fontSize", default = null)
  val layoutEngine: Provider<String> = string(key = "modular.graphviz.chart.layoutEngine", default = null)
  val rankDir: Provider<String> = string(key = "modular.graphviz.chart.rankDir", default = null)
  val rankSep: Provider<Float> = float(key = "modular.graphviz.chart.rankSep", default = null)
  val suppressSvgViewBoxWarning: Provider<Boolean> = bool("modular.graphviz.suppressAdjustSvgViewBox", default = false)
  val writeReadme: Provider<Boolean> = bool(key = "modular.graphviz.writeReadme", default = false)
}

internal class MermaidGradleProperties(override val project: Project) : IGradleProperties {
  val animateLinks: Provider<Boolean> = bool(key = "modular.mermaid.chart.animateLinks", default = false)
  val look: Provider<String> = string(key = "modular.mermaid.chart.look", default = null)
  val theme: Provider<String> = string(key = "modular.mermaid.chart.theme", default = null)
  val writeReadme: Provider<Boolean> = bool(key = "modular.mermaid.writeReadme", default = false)
}

private interface IGradleProperties {
  val project: Project
}

private fun IGradleProperties.bool(key: String, default: Boolean? = null) = prop(key, default, String::toBooleanStrict)
private fun IGradleProperties.float(key: String, default: Float? = null) = prop(key, default, String::toFloat)
private fun IGradleProperties.int(key: String, default: Int? = null) = prop(key, default, String::toInt)
private fun IGradleProperties.string(key: String, default: String? = null) = prop(key, default) { it }

private inline fun <reified T : Any> IGradleProperties.prop(
  key: String,
  default: T?,
  noinline mapper: (String) -> T?,
) = project.providers
  .gradleProperty(key)
  .map(mapper)
  .orElse(project.provider { default })
