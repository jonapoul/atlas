/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.core.InternalModularApi
import modular.core.PropertiesSpec
import modular.core.internal.LinkTypeContainer
import modular.core.internal.ModuleTypeContainer
import modular.core.internal.ModuleTypeSpecImpl
import modular.core.internal.PropertiesSpecImpl
import modular.core.internal.bool
import modular.core.internal.enum
import modular.core.internal.string
import modular.mermaid.ConsiderModelOrder
import modular.mermaid.CycleBreakingStrategy
import modular.mermaid.ElkLayoutSpec
import modular.mermaid.MermaidLayoutSpec
import modular.mermaid.MermaidModuleTypeSpec
import modular.mermaid.MermaidNamedLinkTypeContainer
import modular.mermaid.MermaidNamedModuleTypeContainer
import modular.mermaid.MermaidSpec
import modular.mermaid.MermaidThemeVariablesSpec
import modular.mermaid.NodePlacementStrategy
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

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
  override val look = objects.enum(properties.look)
  override val theme = objects.enum(properties.theme)
}

@InternalModularApi
open class MermaidLayoutSpecImpl(
  objects: ObjectFactory,
) : MermaidLayoutSpec, PropertiesSpec by PropertiesSpecImpl(objects) {
  override val name: Property<String> = objects
    .property(String::class.java)
    .unsetConvention()
}

@InternalModularApi
class ElkLayoutSpecImpl(objects: ObjectFactory) : MermaidLayoutSpecImpl(objects), ElkLayoutSpec {
  init {
    name.set("elk")
    name.finalizeValue()
  }

  override var considerModelOrder by enum<ConsiderModelOrder>("considerModelOrder")
  override var cycleBreakingStrategy by enum<CycleBreakingStrategy>("cycleBreakingStrategy")
  override var forceNodeModelOrder by bool("forceNodeModelOrder")
  override var mergeEdges by bool("mergeEdges")
  override var nodePlacementStrategy by enum<NodePlacementStrategy>("nodePlacementStrategy")
}

@InternalModularApi
class MermaidThemeVariablesSpecImpl(
  objects: ObjectFactory,
) : MermaidThemeVariablesSpec, PropertiesSpec by PropertiesSpecImpl(objects) {
  override var background by string(key = "background")
  override var darkMode by bool(key = "darkMode")
  override var fontFamily by string(key = "fontFamily")
  override var fontSize by string(key = "fontSize")
  override var lineColor by string(key = "lineColor")
  override var primaryBorderColor by string(key = "primaryBorderColor")
  override var primaryColor by string(key = "primaryColor")
  override var primaryTextColor by string(key = "primaryTextColor")
  override var secondaryColor by string(key = "secondaryColor")
  override var tertiaryColor by string(key = "tertiaryColor")
}

@InternalModularApi
class MermaidLinkTypeContainer(objects: ObjectFactory) : LinkTypeContainer(objects), MermaidNamedLinkTypeContainer

@InternalModularApi
abstract class MermaidModuleTypeSpecImpl @Inject constructor(
  override val name: String,
) : ModuleTypeSpecImpl(name), MermaidModuleTypeSpec {
  override var fontColor by string("color")
  override var fontSize by string("font-size")
  override var stroke by string("stroke")
  override var strokeDashArray by string("stroke-dasharray")
  override var strokeWidth by string("stroke-width")
}

@InternalModularApi
class MermaidModuleTypeContainer(objects: ObjectFactory) :
  ModuleTypeContainer<MermaidModuleTypeSpec>(
    delegate = objects.domainObjectContainer(MermaidModuleTypeSpec::class.java) { name ->
      objects.newInstance(MermaidModuleTypeSpecImpl::class.java, name)
    },
  ),
  MermaidNamedModuleTypeContainer
