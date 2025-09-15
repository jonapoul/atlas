/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.internal.GradleProperties
import modular.internal.string
import modular.mermaid.spec.ConsiderModelOrder
import modular.mermaid.spec.CycleBreakingStrategy
import modular.mermaid.spec.ElkLayoutSpec
import modular.mermaid.spec.MermaidChartSpec
import modular.mermaid.spec.MermaidLayoutSpec
import modular.mermaid.spec.MermaidLegendSpec
import modular.mermaid.spec.MermaidSpec
import modular.mermaid.spec.NodePlacementStrategy
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

internal class MermaidSpecImpl(
  objects: ObjectFactory,
  properties: GradleProperties,
) : MermaidSpec {
  override val name = NAME
  override val fileExtension = objects.string(convention = "mmd")

  override var legend: MermaidLegendSpec? = null
  override fun legend() = legend { /* no-op */ }
  override fun legend(action: Action<MermaidLegendSpec>) = action.execute(getOrBuildLegend())

  override val chart = MermaidChartSpecImpl(objects, properties)

  private fun getOrBuildLegend() = legend ?: MermaidLegendSpecImpl().also { legend = it }

  internal companion object {
    internal const val NAME = "MermaidSpecImpl"
  }
}

internal class MermaidLegendSpecImpl : MermaidLegendSpec {
  // TBC
}

internal class MermaidChartSpecImpl(
  private val objects: ObjectFactory,
  properties: GradleProperties,
) : MermaidChartSpec {
  private var mutableLayout = MermaidLayoutSpecImpl(objects)

  override val layout get() = mutableLayout
  override fun layout(action: Action<MermaidLayoutSpec>) = action.execute(layout)

  override fun elk(action: Action<ElkLayoutSpec>?) {
    mutableLayout = ElkLayoutSpecImpl(objects).also { action?.execute(it) }
  }

  override val look = objects.string(properties.mermaidLook)
  override val theme = objects.string(properties.mermaidTheme)
}

internal open class MermaidLayoutSpecImpl(objects: ObjectFactory) : MermaidLayoutSpec {
  override val name: Property<String> = objects
    .property(String::class.java)
    .unsetConvention()

  override val properties = objects
    .mapProperty(String::class.java, String::class.java)
    .convention(null)
}

internal class ElkLayoutSpecImpl(objects: ObjectFactory) : MermaidLayoutSpecImpl(objects), ElkLayoutSpec {
  init {
    name.set("elk")
    name.finalizeValue()
  }

  override fun considerModelOrder(order: ConsiderModelOrder) = put("considerModelOrder", order.string)
  override fun cycleBreakingStrategy(strategy: CycleBreakingStrategy) = put("cycleBreakingStrategy", strategy.string)
  override fun forceNodeModelOrder(enabled: Boolean) = put("forceNodeModelOrder", enabled)
  override fun mergeEdges(enabled: Boolean) = put("mergeEdges", enabled)
  override fun nodePlacementStrategy(strategy: NodePlacementStrategy) = put("nodePlacementStrategy", strategy.string)
}
