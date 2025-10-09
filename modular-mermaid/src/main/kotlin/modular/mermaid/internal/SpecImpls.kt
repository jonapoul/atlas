/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.core.InternalModularApi
import modular.core.internal.LinkTypeContainer
import modular.core.internal.ModuleTypeContainer
import modular.core.internal.ModuleTypeSpecImpl
import modular.core.internal.PropertiesSpec
import modular.core.internal.PropertiesSpecImpl
import modular.core.internal.bool
import modular.core.internal.boolDelegate
import modular.core.internal.enum
import modular.core.internal.enumDelegate
import modular.core.internal.string
import modular.core.internal.stringDelegate
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
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import javax.inject.Inject
import kotlin.jvm.java

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

  override var considerModelOrder by enumDelegate<ConsiderModelOrder>("considerModelOrder")
  override var cycleBreakingStrategy by enumDelegate<CycleBreakingStrategy>("cycleBreakingStrategy")
  override var forceNodeModelOrder by boolDelegate("forceNodeModelOrder")
  override var mergeEdges by boolDelegate("mergeEdges")
  override var nodePlacementStrategy by enumDelegate<NodePlacementStrategy>("nodePlacementStrategy")
}

@InternalModularApi
class MermaidThemeVariablesSpecImpl(
  objects: ObjectFactory,
) : MermaidThemeVariablesSpec, PropertiesSpec by PropertiesSpecImpl(objects) {
  override var background by stringDelegate(key = "background")
  override var darkMode by boolDelegate(key = "darkMode")
  override var fontFamily by stringDelegate(key = "fontFamily")
  override var fontSize by stringDelegate(key = "fontSize")
  override var lineColor by stringDelegate(key = "lineColor")
  override var primaryBorderColor by stringDelegate(key = "primaryBorderColor")
  override var primaryColor by stringDelegate(key = "primaryColor")
  override var primaryTextColor by stringDelegate(key = "primaryTextColor")
  override var secondaryColor by stringDelegate(key = "secondaryColor")
  override var tertiaryColor by stringDelegate(key = "tertiaryColor")
}

@InternalModularApi
class MermaidLinkTypeContainer(objects: ObjectFactory) : LinkTypeContainer(objects), MermaidNamedLinkTypeContainer


@InternalModularApi
abstract class MermaidModuleTypeSpecImpl @Inject constructor(
  override val name: String,
) : ModuleTypeSpecImpl(name), MermaidModuleTypeSpec {
  @get:Input abstract override val properties: MapProperty<String, String>

  override fun put(key: String, value: Any) = properties.put(key, value.toString())

  override var fontColor by stringDelegate("color")
  override var fontSize by stringDelegate("font-size")
  override var stroke by stringDelegate("stroke")
  override var strokeDashArray by stringDelegate("stroke-dasharray")
  override var strokeWidth by stringDelegate("stroke-width")

  init {
    properties.convention(emptyMap())
  }
}

@InternalModularApi
class MermaidModuleTypeContainer(objects: ObjectFactory) :
  ModuleTypeContainer<MermaidModuleTypeSpec>(
    delegate = objects.domainObjectContainer(MermaidModuleTypeSpec::class.java) { name ->
      objects.newInstance(MermaidModuleTypeSpecImpl::class.java, name)
    },
  ),
  MermaidNamedModuleTypeContainer
