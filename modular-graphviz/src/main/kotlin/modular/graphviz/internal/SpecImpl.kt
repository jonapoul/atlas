/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.InternalModularApi
import modular.core.internal.LinkTypeContainer
import modular.core.internal.ModuleTypeContainer
import modular.core.internal.ModuleTypeSpecImpl
import modular.core.internal.enum
import modular.core.internal.string
import modular.graphviz.EdgeAttributes
import modular.graphviz.GraphAttributes
import modular.graphviz.GraphvizModuleTypeSpec
import modular.graphviz.GraphvizNamedLinkTypeContainer
import modular.graphviz.GraphvizNamedModuleTypeContainer
import modular.graphviz.GraphvizSpec
import modular.graphviz.NodeAttributes
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

@InternalModularApi
class GraphvizSpecImpl(
  objects: ObjectFactory,
  project: Project,
) : GraphvizSpec {
  internal val properties = GraphvizGradleProperties(project)

  override val name = "Graphviz"
  override val fileExtension = objects.string(convention = "dot")
  override val pathToDotCommand: Property<String> = objects.string(convention = null)
  override val fileFormat = objects.enum(properties.fileFormat)
  override val layoutEngine = objects.enum(properties.layoutEngine)

  override val node = NodeAttributesImpl(objects)
  override fun node(action: Action<NodeAttributes>) = action.execute(node)

  override val edge = EdgeAttributesImpl(objects)
  override fun edge(action: Action<EdgeAttributes>) = action.execute(edge)

  override val graph = GraphAttributesImpl(objects)
  override fun graph(action: Action<GraphAttributes>) = action.execute(graph)
}

@InternalModularApi
class GraphvizLinkTypeContainer(
  objects: ObjectFactory,
) : LinkTypeContainer(objects), GraphvizNamedLinkTypeContainer

@InternalModularApi
abstract class GraphvizModuleTypeSpecImpl @Inject constructor(
  override val name: String,
  objects: ObjectFactory,
) : ModuleTypeSpecImpl(name), GraphvizModuleTypeSpec, NodeAttributes by NodeAttributesImpl(objects)

@InternalModularApi
class GraphvizModuleTypeContainer(objects: ObjectFactory) :
  ModuleTypeContainer<GraphvizModuleTypeSpec>(
    delegate = objects.domainObjectContainer(GraphvizModuleTypeSpec::class.java) { name ->
      objects.newInstance(GraphvizModuleTypeSpecImpl::class.java, name)
    },
  ),
  GraphvizNamedModuleTypeContainer
