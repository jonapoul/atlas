package atlas.d2.internal

import atlas.core.PropertiesSpec
import atlas.core.internal.LinkTypeContainer
import atlas.core.internal.ProjectTypeContainer
import atlas.core.internal.ProjectTypeSpecImpl
import atlas.core.internal.PropertiesSpecImpl
import atlas.core.internal.bool
import atlas.core.internal.enum
import atlas.core.internal.float
import atlas.core.internal.int
import atlas.core.internal.intEnum
import atlas.core.internal.string
import atlas.d2.ArrowType
import atlas.d2.D2DagreSpec
import atlas.d2.D2ElkSpec
import atlas.d2.D2GlobalPropsSpec
import atlas.d2.D2LayoutEngineSpec
import atlas.d2.D2LinkTypeSpec
import atlas.d2.D2NamedLinkTypeContainer
import atlas.d2.D2NamedProjectTypeContainer
import atlas.d2.D2ProjectTypeSpec
import atlas.d2.D2PropertiesSpec
import atlas.d2.D2RootStyleSpec
import atlas.d2.D2Spec
import atlas.d2.D2TalaSpec
import atlas.d2.ElkAlgorithm
import atlas.d2.FillPattern
import atlas.d2.Font
import atlas.d2.LayoutEngine
import atlas.d2.Shape
import atlas.d2.TextTransform
import atlas.d2.tasks.SvgToPng
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

internal class D2SpecImpl(
  objects: ObjectFactory,
  project: Project,
) : D2Spec {
  internal val properties = D2GradleProperties(project)

  override val name = "D2"
  override val fileExtension = objects.string(convention = "d2")

  override val animateLinks = objects.bool(properties.animateLinks)
  override val animateInterval = objects.int(properties.animateInterval)
  override val center = objects.bool(properties.center)
  override val direction = objects.enum(properties.direction)
  override val fileFormat = objects.enum(properties.fileFormat)
  override val groupLabelLocation = objects.enum(properties.groupLabelLocation)
  override val groupLabelPosition = objects.enum(properties.groupLabelPosition)
  override val pad = objects.int(properties.pad)
  override val pathToD2Command = objects.string(properties.pathToD2Command)
  override val sketch = objects.bool(properties.sketch)
  override val theme = objects.intEnum(properties.theme)
  override val themeDark = objects.intEnum(properties.darkTheme)

  override val layoutEngine = D2LayoutEngineSpecImpl(objects)
  override fun layoutEngine(action: Action<D2LayoutEngineSpec>) = action.execute(layoutEngine)

  override val rootStyle = D2RootStyleSpecImpl(objects)
  override fun rootStyle(action: Action<D2RootStyleSpec>) = action.execute(rootStyle)

  override val globalProps = D2GlobalPropsSpecImpl(objects)
  override fun globalProps(action: Action<D2GlobalPropsSpec>) = action.execute(globalProps)

  internal val converter = objects.property(SvgToPng.Converter::class.java)
  override fun convertSvgToPng(converter: SvgToPng.Converter) = this.converter.set(converter)
}

internal open class D2RootStyleSpecImpl(
  objects: ObjectFactory,
) : D2RootStyleSpec, PropertiesSpec by PropertiesSpecImpl(objects) {
  override var fill by string("fill")
  override var fillPattern by enum<FillPattern>("fill-pattern")
  override var stroke by string("stroke")
  override var strokeWidth by int("stroke-width")
  override var strokeDash by int("stroke-dash")
  override var doubleBorder by bool("double-border")
}

internal open class D2GlobalPropsSpecImpl(
  objects: ObjectFactory,
) : D2GlobalPropsSpec, PropertiesSpec by PropertiesSpecImpl(objects) {
  override var arrowType by enum<ArrowType>("(** -> **)[*].target-arrowhead.shape")
  override var fillArrowHeads by bool("(** -> **)[*].target-arrowhead.style.filled")
  override var font by enum<Font>("***.style.font")
  override var fontSize by int("***.style.font-size")
}

internal class D2PropertiesSpecImpl(
  objects: ObjectFactory,
) : D2PropertiesSpec, PropertiesSpec by PropertiesSpecImpl(objects) {
  override var animated by bool("style.animated")
  override var bold by bool("style.bold")
  override var borderRadius by int("style.border-radius")
  override var font by enum<Font>("style.font")
  override var fontColor by string("style.font-color")
  override var fontSize by int("style.font-size")
  override var italic by bool("style.italic")
  override var opacity by float("style.opacity")
  override var stroke by string("style.stroke")
  override var strokeDash by int("style.stroke-dash")
  override var strokeWidth by int("style.stroke-width")
  override var textTransform by enum<TextTransform>("style.text-transform")
  override var underline by bool("style.underline")
}

internal abstract class D2ProjectTypeSpecImpl @Inject constructor(
  override val name: String,
  objects: ObjectFactory,
) : ProjectTypeSpecImpl(name), D2ProjectTypeSpec, D2PropertiesSpec by D2PropertiesSpecImpl(objects) {
  override var doubleBorder by string("style.double-border")
  override var fill by string("style.fill")
  override var fillPattern by enum<FillPattern>("style.fill-pattern")
  override var multiple by bool("style.multiple")
  override var render3D by bool("style.3d")
  override var shadow by bool("style.shadow")
  override var shape by enum<Shape>("shape")
}

internal class D2NamedProjectTypeContainerImpl(objects: ObjectFactory) :
  ProjectTypeContainer<D2ProjectTypeSpec>(
    delegate = objects.domainObjectContainer(D2ProjectTypeSpec::class.java) { name ->
      objects.newInstance(D2ProjectTypeSpecImpl::class.java, name)
    },
  ),
  D2NamedProjectTypeContainer

internal abstract class D2LinkTypeSpecImpl @Inject constructor(
  override val name: String,
  objects: ObjectFactory,
) : ProjectTypeSpecImpl(name), D2LinkTypeSpec, D2PropertiesSpec by D2PropertiesSpecImpl(objects)

internal class D2NamedLinkTypeContainerImpl(
  objects: ObjectFactory,
) : LinkTypeContainer<D2LinkTypeSpec>(
    delegate = objects.domainObjectContainer(D2LinkTypeSpec::class.java) { name ->
      objects.newInstance(D2LinkTypeSpecImpl::class.java, name)
    },
  ),
  D2NamedLinkTypeContainer

internal class D2LayoutEngineSpecImpl(
  objects: ObjectFactory,
) : D2LayoutEngineSpec, PropertiesSpec by PropertiesSpecImpl(objects) {
  override val layoutEngine = objects.enum<LayoutEngine>(convention = null)
  override val dagre = D2DagreSpecImpl(this)
  override val elk = D2ElkSpecImpl(this)
  override val tala = D2TalaSpecImpl(this)

  override fun dagre(config: Action<D2DagreSpec>?) {
    layoutEngine.set(LayoutEngine.Dagre)
    config?.execute(dagre)
  }

  override fun elk(config: Action<D2ElkSpec>?) {
    layoutEngine.set(LayoutEngine.Elk)
    config?.execute(elk)
  }

  override fun tala(config: Action<D2TalaSpec>?) {
    layoutEngine.set(LayoutEngine.Tala)
    config?.execute(tala)
  }
}

internal class D2ElkSpecImpl(parent: PropertiesSpec) : D2ElkSpec, PropertiesSpec by parent {
  override var algorithm by enum<ElkAlgorithm>("elk-algorithm")
  override var edgeNodeBetweenLayers by int("elk-edgeNodeBetweenLayers")
  override var nodeNodeBetweenLayers by int("elk-nodeNodeBetweenLayers")
  override var nodeSelfLoop by int("elk-nodeSelfLoop")
  override var padding by string("elk-padding")
}

internal class D2DagreSpecImpl(parent: PropertiesSpec) : D2DagreSpec, PropertiesSpec by parent {
  override var nodeSep by int("dagre-nodesep")
  override var edgeSep by int("dagre-edgesep")
}

internal class D2TalaSpecImpl(parent: PropertiesSpec) : D2TalaSpec, PropertiesSpec by parent
