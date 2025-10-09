/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.internal

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
import modular.core.internal.floatDelegate
import modular.core.internal.int
import modular.core.internal.intDelegate
import modular.core.internal.intEnum
import modular.core.internal.string
import modular.core.internal.stringDelegate
import modular.d2.ArrowType
import modular.d2.D2GlobalPropsSpec
import modular.d2.D2ModuleTypeSpec
import modular.d2.D2NamedLinkTypeContainer
import modular.d2.D2NamedModuleTypeContainer
import modular.d2.D2RootStyleSpec
import modular.d2.D2Spec
import modular.d2.FillPattern
import modular.d2.Font
import modular.d2.TextTransform
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Input
import javax.inject.Inject

@InternalModularApi
class D2SpecImpl(
  objects: ObjectFactory,
  project: Project,
) : D2Spec {
  internal val properties = D2GradleProperties(project)

  override val name = "D2"
  override val fileExtension = objects.string(convention = "d2")

  override val animateLinks = objects.bool(properties.animateLinks)
  override val center = objects.bool(properties.center)
  override val direction = objects.enum(properties.direction)
  override val fileFormat = objects.enum(properties.fileFormat)
  override val groupLabelLocation = objects.enum(properties.groupLabelLocation)
  override val groupLabelPosition = objects.enum(properties.groupLabelPosition)
  override val layoutEngine = objects.enum(properties.layoutEngine)
  override val pad = objects.int(properties.pad)
  override val pathToD2Command = objects.string(properties.pathToD2Command)
  override val sketch = objects.bool(properties.sketch)
  override val theme = objects.intEnum(properties.theme)
  override val themeDark = objects.intEnum(properties.darkTheme)

  override val rootStyle = D2RootStyleSpecImpl(objects)
  override fun rootStyle(action: Action<D2RootStyleSpec>) = action.execute(rootStyle)

  override val globalProps = D2GlobalPropsSpecImpl(objects)
  override fun globalProps(action: Action<D2GlobalPropsSpec>) = action.execute(globalProps)
}

@InternalModularApi
open class D2RootStyleSpecImpl(
  objects: ObjectFactory,
) : D2RootStyleSpec, PropertiesSpec by PropertiesSpecImpl(objects) {
  override var fill by stringDelegate("fill")
  override var fillPattern by enumDelegate<FillPattern>("fill-pattern")
  override var stroke by stringDelegate("stroke")
  override var strokeWidth by intDelegate("stroke-width")
  override var strokeDash by intDelegate("stroke-dash")
  override var doubleBorder by boolDelegate("double-border")
}

@InternalModularApi
open class D2GlobalPropsSpecImpl(
  objects: ObjectFactory,
) : D2GlobalPropsSpec, PropertiesSpec by PropertiesSpecImpl(objects) {
  override var arrowType by enumDelegate<ArrowType>("(** -> **)[*].target-arrowhead.shape")
  override var fillArrowHeads by boolDelegate("(** -> **)[*].target-arrowhead.style.filled")
  override var font by enumDelegate<Font>("***.style.font")
  override var fontSize by intDelegate("***.style.font-size")
}

@InternalModularApi
class D2LinkTypeContainer(
  objects: ObjectFactory,
) : LinkTypeContainer(objects), D2NamedLinkTypeContainer

@InternalModularApi
abstract class D2ModuleTypeSpecImpl @Inject constructor(
  override val name: String,
) : ModuleTypeSpecImpl(name), D2ModuleTypeSpec {
  @get:Input abstract override val properties: MapProperty<String, String>

  override fun put(key: String, value: Any) = properties.put(key, value.toString())

  override var animated by boolDelegate("style.animated")
  override var bold by boolDelegate("style.bold")
  override var borderRadius by intDelegate("style.border-radius")
  override var doubleBorder by stringDelegate("style.double-border")
  override var fillPattern by enumDelegate<FillPattern>("style.fill-pattern")
  override var font by enumDelegate<Font>("style.font")
  override var fontColor by stringDelegate("style.font-color")
  override var fontSize by intDelegate("style.font-size")
  override var italic by boolDelegate("style.italic")
  override var multiple by boolDelegate("style.multiple")
  override var opacity by floatDelegate("style.opacity")
  override var render3D by boolDelegate("style.3d")
  override var shadow by boolDelegate("style.shadow")
  override var stroke by stringDelegate("style.stroke")
  override var strokeDash by intDelegate("style.stroke-dash")
  override var strokeWidth by intDelegate("style.stroke-width")
  override var textTransform by enumDelegate<TextTransform>("style.text-transform")
  override var underline by boolDelegate("style.underline")

  init {
    properties.convention(emptyMap())
  }
}

@InternalModularApi
class D2ModuleTypeContainer(objects: ObjectFactory) : ModuleTypeContainer<D2ModuleTypeSpec>(
  delegate = objects.domainObjectContainer(D2ModuleTypeSpec::class.java) { name ->
    objects.newInstance(D2ModuleTypeSpecImpl::class.java, name)
  },
), D2NamedModuleTypeContainer
