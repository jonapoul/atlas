package atlas.mermaid.internal

import atlas.core.PropertiesSpec
import atlas.core.internal.LinkTypeContainer
import atlas.core.internal.ModuleTypeContainer
import atlas.core.internal.ModuleTypeSpecImpl
import atlas.core.internal.PropertiesSpecImpl
import atlas.core.internal.bool
import atlas.core.internal.enum
import atlas.core.internal.string
import atlas.mermaid.ConsiderModelOrder
import atlas.mermaid.CycleBreakingStrategy
import atlas.mermaid.ElkLayoutSpec
import atlas.mermaid.MermaidLayoutSpec
import atlas.mermaid.MermaidLinkTypeSpec
import atlas.mermaid.MermaidModuleTypeSpec
import atlas.mermaid.MermaidNamedLinkTypeContainer
import atlas.mermaid.MermaidNamedModuleTypeContainer
import atlas.mermaid.MermaidSpec
import atlas.mermaid.MermaidThemeVariablesSpec
import atlas.mermaid.NodePlacementStrategy
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject
import kotlin.jvm.java

internal class MermaidSpecImpl(
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

internal open class MermaidLayoutSpecImpl(
  objects: ObjectFactory,
) : MermaidLayoutSpec, PropertiesSpec by PropertiesSpecImpl(objects) {
  override val name = objects
    .property(String::class.java)
    .unsetConvention()
}

internal class ElkLayoutSpecImpl(objects: ObjectFactory) : MermaidLayoutSpecImpl(objects), ElkLayoutSpec {
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

internal class MermaidThemeVariablesSpecImpl(
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

internal abstract class MermaidModuleTypeSpecImpl @Inject constructor(
  override val name: String,
) : ModuleTypeSpecImpl(name), MermaidModuleTypeSpec {
  override var fontColor by string("color")
  override var fontSize by string("font-size")
  override var stroke by string("stroke")
  override var strokeDashArray by string("stroke-dasharray")
  override var strokeWidth by string("stroke-width")
}

internal class MermaidNamedModuleTypeContainerImpl(objects: ObjectFactory) :
  ModuleTypeContainer<MermaidModuleTypeSpec>(
    delegate = objects.domainObjectContainer(MermaidModuleTypeSpec::class.java) { name ->
      objects.newInstance(MermaidModuleTypeSpecImpl::class.java, name)
    },
  ),
  MermaidNamedModuleTypeContainer

internal abstract class MermaidLinkTypeSpecImpl @Inject constructor(
  override val name: String,
  objects: ObjectFactory,
) : ModuleTypeSpecImpl(name), MermaidLinkTypeSpec, PropertiesSpec by PropertiesSpecImpl(objects) {
  override var fontColor by string("color")
  override var stroke by string("stroke")
  override var strokeWidth by string("stroke-width")
  override var strokeDashArray by string("stroke-dasharray")
}

internal class MermaidNamedLinkTypeContainerImpl(
  objects: ObjectFactory,
) : LinkTypeContainer<MermaidLinkTypeSpec>(
    delegate = objects.domainObjectContainer(MermaidLinkTypeSpec::class.java) { name ->
      objects.newInstance(MermaidLinkTypeSpecImpl::class.java, name)
    },
  ),
  MermaidNamedLinkTypeContainer
