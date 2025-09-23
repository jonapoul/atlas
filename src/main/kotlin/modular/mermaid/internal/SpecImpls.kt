/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.core.internal.GradleProperties
import modular.core.internal.bool
import modular.core.internal.string
import modular.mermaid.spec.ConsiderModelOrder
import modular.mermaid.spec.CycleBreakingStrategy
import modular.mermaid.spec.ElkLayoutSpec
import modular.mermaid.spec.MermaidLayoutSpec
import modular.mermaid.spec.MermaidSpec
import modular.mermaid.spec.NodePlacementStrategy
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

internal class MermaidSpecImpl(
  private val objects: ObjectFactory,
  properties: GradleProperties,
) : MermaidSpec {
  private var mutableLayout = MermaidLayoutSpecImpl(objects)

  override val name = NAME
  override val fileExtension = objects.string(convention = "mmd")
  override val writeReadme = objects.bool(convention = properties.mermaid.writeReadme)

  override val layout get() = mutableLayout
  override fun layout(action: Action<MermaidLayoutSpec>) = action.execute(layout)

  override fun elk(action: Action<ElkLayoutSpec>?) {
    mutableLayout = ElkLayoutSpecImpl(objects).also { action?.execute(it) }
  }

  override val animateLinks = objects.bool(properties.mermaid.animateLinks)
  override val look = objects.string(properties.mermaid.look)
  override val theme = objects.string(properties.mermaid.theme)

  internal companion object {
    internal const val NAME = "Mermaid"
  }
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
