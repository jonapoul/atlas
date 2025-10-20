package atlas.graphviz.internal

import atlas.core.internal.AtlasExtensionImpl
import atlas.graphviz.GraphvizAtlasExtension
import atlas.graphviz.GraphvizNamedLinkTypeContainer
import atlas.graphviz.GraphvizNamedModuleTypeContainer
import atlas.graphviz.GraphvizSpec
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

internal open class GraphvizAtlasExtensionImpl @Inject constructor(
  objects: ObjectFactory,
  project: Project,
) : AtlasExtensionImpl(objects, project), GraphvizAtlasExtension {
  override val graphviz = GraphvizSpecImpl(objects, project)
  override fun graphviz(action: Action<GraphvizSpec>) = action.execute(graphviz)

  override val linkTypes = GraphvizNamedLinkTypeContainerImpl(objects)
  override fun linkTypes(action: Action<GraphvizNamedLinkTypeContainer>) = action.execute(linkTypes)

  override val moduleTypes = GraphvizNamedModuleTypeContainerImpl(objects)
  override fun moduleTypes(action: Action<GraphvizNamedModuleTypeContainer>) = action.execute(moduleTypes)
}
