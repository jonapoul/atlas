/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.graphviz.internal

import atlas.core.internal.LinkTypeContainer
import atlas.core.internal.ModuleTypeContainer
import atlas.core.internal.ModuleTypeSpecImpl
import atlas.core.internal.enum
import atlas.core.internal.string
import atlas.graphviz.EdgeAttributes
import atlas.graphviz.FileFormat
import atlas.graphviz.GraphAttributes
import atlas.graphviz.GraphvizLinkTypeSpec
import atlas.graphviz.GraphvizModuleTypeSpec
import atlas.graphviz.GraphvizNamedLinkTypeContainer
import atlas.graphviz.GraphvizNamedModuleTypeContainer
import atlas.graphviz.GraphvizSpec
import atlas.graphviz.LayoutEngine
import atlas.graphviz.NodeAttributes
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

internal class GraphvizSpecImpl(
  objects: ObjectFactory,
  project: Project,
) : GraphvizSpec {
  internal val properties = GraphvizGradleProperties(project)

  override val name: String = "Graphviz"
  override val fileExtension: Property<String> = objects.string(convention = "dot")
  override val pathToDotCommand: Property<String> = objects.string(convention = null)
  override val fileFormat: Property<FileFormat> = objects.enum(properties.fileFormat)
  override val layoutEngine: Property<LayoutEngine> = objects.enum(properties.layoutEngine)

  override val node = NodeAttributesImpl(objects)
  override fun node(action: Action<NodeAttributes>) = action.execute(node)

  override val edge = EdgeAttributesImpl(objects)
  override fun edge(action: Action<EdgeAttributes>) = action.execute(edge)

  override val graph = GraphAttributesImpl(objects)
  override fun graph(action: Action<GraphAttributes>) = action.execute(graph)
}

internal abstract class GraphvizModuleTypeSpecImpl @Inject constructor(
  override val name: String,
  objects: ObjectFactory,
) : ModuleTypeSpecImpl(name), GraphvizModuleTypeSpec, NodeAttributes by NodeAttributesImpl(objects)

internal class GraphvizNamedModuleTypeContainerImpl(objects: ObjectFactory) :
  ModuleTypeContainer<GraphvizModuleTypeSpec>(
    delegate = objects.domainObjectContainer(GraphvizModuleTypeSpec::class.java) { name ->
      objects.newInstance(GraphvizModuleTypeSpecImpl::class.java, name)
    },
  ),
  GraphvizNamedModuleTypeContainer

internal abstract class GraphvizLinkTypeSpecImpl @Inject constructor(
  override val name: String,
  objects: ObjectFactory,
) : ModuleTypeSpecImpl(name), GraphvizLinkTypeSpec, EdgeAttributes by EdgeAttributesImpl(objects)

internal class GraphvizNamedLinkTypeContainerImpl(
  objects: ObjectFactory,
) : LinkTypeContainer<GraphvizLinkTypeSpec>(
    delegate = objects.domainObjectContainer(GraphvizLinkTypeSpec::class.java) { name ->
      objects.newInstance(GraphvizLinkTypeSpecImpl::class.java, name)
    },
  ),
  GraphvizNamedLinkTypeContainer
