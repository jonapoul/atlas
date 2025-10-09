/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.InternalModularApi
import modular.core.internal.LinkTypeContainer
import modular.core.internal.ModuleTypeContainer
import modular.core.internal.ModuleTypeSpecImpl
import modular.core.internal.bool
import modular.core.internal.boolDelegate
import modular.core.internal.enum
import modular.core.internal.enumDelegate
import modular.core.internal.float
import modular.core.internal.floatDelegate
import modular.core.internal.int
import modular.core.internal.intDelegate
import modular.core.internal.string
import modular.core.internal.stringDelegate
import modular.graphviz.GraphvizModuleTypeSpec
import modular.graphviz.GraphvizNamedLinkTypeContainer
import modular.graphviz.GraphvizNamedModuleTypeContainer
import modular.graphviz.GraphvizSpec
import modular.graphviz.Shape
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import javax.inject.Inject

@InternalModularApi
class GraphvizSpecImpl(
  objects: ObjectFactory,
  project: Project,
) : GraphvizSpec {
  internal val properties = GraphvizGradleProperties(project)

  override val name = "Graphviz"
  override val fileExtension = objects.string(convention = "dot")
  override val pathToDotCommand: Property<String> = objects.property(String::class.java).unsetConvention()
  override val backgroundColor: Property<String> = objects.property(String::class.java).unsetConvention()
  override val adjustSvgViewBox = objects.bool(convention = properties.adjustSvgViewBox)

  override val arrowHead = objects.enum(properties.arrowHead)
  override val arrowTail = objects.enum(properties.arrowTail)
  override val dir = objects.enum(properties.dir)
  override val dpi = objects.int(properties.dpi)
  override val fileFormat = objects.enum(properties.fileFormat)
  override val fontSize = objects.int(properties.fontSize)
  override val layoutEngine = objects.enum(properties.layoutEngine)
  override val rankDir = objects.enum(properties.rankDir)
  override val rankSep = objects.float(properties.rankSep)
}

@InternalModularApi
class GraphvizLinkTypeContainer(
  objects: ObjectFactory,
) : LinkTypeContainer(objects), GraphvizNamedLinkTypeContainer

@InternalModularApi
abstract class GraphvizModuleTypeSpecImpl @Inject constructor(
  override val name: String,
) : ModuleTypeSpecImpl(name), GraphvizModuleTypeSpec {
  @get:Input abstract override val properties: MapProperty<String, String>

  override fun put(key: String, value: Any) = properties.put(key, value.toString())

  override var shape by enumDelegate<Shape>("shape")

  init {
    properties.convention(emptyMap())
  }
}

@InternalModularApi
class GraphvizModuleTypeContainer(objects: ObjectFactory) :
  ModuleTypeContainer<GraphvizModuleTypeSpec>(
    delegate = objects.domainObjectContainer(GraphvizModuleTypeSpec::class.java) { name ->
      objects.newInstance(GraphvizModuleTypeSpecImpl::class.java, name)
    },
  ),
  GraphvizNamedModuleTypeContainer
