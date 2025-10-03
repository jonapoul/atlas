/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.core.InternalModularApi
import modular.core.internal.bool
import modular.core.internal.string
import modular.mermaid.ConsiderModelOrder
import modular.mermaid.CycleBreakingStrategy
import modular.mermaid.ElkLayoutSpec
import modular.mermaid.MermaidLayoutSpec
import modular.mermaid.MermaidSpec
import modular.mermaid.MermaidThemeVariablesSpec
import modular.mermaid.NodePlacementStrategy
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

@InternalModularApi
class MermaidSpecImpl(
  private val objects: ObjectFactory,
  project: Project,
) : MermaidSpec {
  private val properties = MermaidGradleProperties(project)
  private var mutableLayout = MermaidLayoutSpecImpl(objects)

  override val name = "Mermaid"
  override val fileExtension = objects.string(convention = "mmd")

  override val layout get() = mutableLayout
  override fun layout(action: Action<MermaidLayoutSpec>) = action.execute(mutableLayout)

  override val themeVariables = MermaidThemeVariablesSpecImpl(objects)
  override fun themeVariables(action: Action<MermaidThemeVariablesSpec>) = action.execute(themeVariables)

  override fun elk(action: Action<ElkLayoutSpec>?) {
    mutableLayout = ElkLayoutSpecImpl(objects).also { action?.execute(it) }
  }

  override val animateLinks = objects.bool(properties.animateLinks)
  override val look = objects.string(properties.look)
  override val theme = objects.string(properties.theme)
}

@InternalModularApi
open class MermaidLayoutSpecImpl(objects: ObjectFactory) : MermaidLayoutSpec {
  override val name: Property<String> = objects
    .property(String::class.java)
    .unsetConvention()

  override val properties = objects
    .mapProperty(String::class.java, String::class.java)
    .convention(null)
}

@InternalModularApi
class ElkLayoutSpecImpl(objects: ObjectFactory) : MermaidLayoutSpecImpl(objects), ElkLayoutSpec {
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

@InternalModularApi
class MermaidThemeVariablesSpecImpl(objects: ObjectFactory) : MermaidThemeVariablesSpec {
  override val properties = objects
    .mapProperty(String::class.java, String::class.java)
    .convention(null)

  override fun background(value: String) = put("background", value)
  override fun darkMode(value: Boolean) = put("darkMode", value)
  override fun fontFamily(value: String) = put("fontFamily", value)
  override fun fontSize(value: String) = put("fontSize", value)
  override fun lineColor(value: String) = put("lineColor", value)
  override fun primaryBorderColor(value: String) = put("primaryBorderColor", value)
  override fun primaryColor(value: String) = put("primaryColor", value)
  override fun primaryTextColor(value: String) = put("primaryTextColor", value)
  override fun secondaryColor(value: String) = put("secondaryColor", value)
  override fun tertiaryColor(value: String) = put("tertiaryColor", value)
}
