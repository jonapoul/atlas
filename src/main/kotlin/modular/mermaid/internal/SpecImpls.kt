/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.internal.GradleProperties
import modular.internal.string
import modular.mermaid.spec.MermaidChartSpec
import modular.mermaid.spec.MermaidLegendSpec
import modular.mermaid.spec.MermaidSpec
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory

internal class MermaidSpecImpl(
  private val objects: ObjectFactory,
  private val properties: GradleProperties,
) : MermaidSpec {
  override val name = NAME
  override val fileExtension = objects.string(convention = "mmd")

  override var legend: MermaidLegendSpec? = null
  override fun legend() = legend { /* no-op */ }
  override fun legend(action: Action<MermaidLegendSpec>) = action.execute(getOrBuildLegend())

  override val chart = MermaidChartSpecImpl(objects, properties)

  private fun getOrBuildLegend() = legend ?: MermaidLegendSpecImpl(objects, properties).also { legend = it }

  internal companion object {
    internal const val NAME = "MermaidSpecImpl"
  }
}

internal class MermaidLegendSpecImpl(objects: ObjectFactory, properties: GradleProperties) : MermaidLegendSpec {
  // TBC
}

internal class MermaidChartSpecImpl(objects: ObjectFactory, properties: GradleProperties) : MermaidChartSpec {
  // TBC
}
