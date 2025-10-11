/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import modular.core.internal.ModularExtensionImpl
import modular.graphviz.GraphvizModularExtension
import modular.graphviz.GraphvizNamedLinkTypeContainer
import modular.graphviz.GraphvizNamedModuleTypeContainer
import modular.graphviz.GraphvizSpec
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

internal open class GraphvizModularExtensionImpl @Inject constructor(
  objects: ObjectFactory,
  project: Project,
) : ModularExtensionImpl(objects, project), GraphvizModularExtension {
  override val graphviz: GraphvizSpecImpl = GraphvizSpecImpl(objects, project)
  override fun graphviz(action: Action<GraphvizSpec>) = action.execute(graphviz)

  override val linkTypes: GraphvizLinkTypeContainer = GraphvizLinkTypeContainer(objects)
  override fun linkTypes(action: Action<GraphvizNamedLinkTypeContainer>) = action.execute(linkTypes)

  override val moduleTypes: GraphvizModuleTypeContainer = GraphvizModuleTypeContainer(objects)
  override fun moduleTypes(action: Action<GraphvizNamedModuleTypeContainer>) = action.execute(moduleTypes)
}
