package atlas.mermaid.internal

import atlas.core.internal.AtlasExtensionImpl
import atlas.mermaid.MermaidAtlasExtension
import atlas.mermaid.MermaidNamedLinkTypeContainer
import atlas.mermaid.MermaidNamedProjectTypeContainer
import atlas.mermaid.MermaidSpec
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

internal open class MermaidAtlasExtensionImpl @Inject constructor(
  objects: ObjectFactory,
  project: Project,
) : AtlasExtensionImpl(objects, project), MermaidAtlasExtension {
  override val mermaid = MermaidSpecImpl(objects, project)
  override fun mermaid(action: Action<MermaidSpec>) = action.execute(mermaid)

  override val linkTypes = MermaidNamedLinkTypeContainerImpl(objects)
  override fun linkTypes(action: Action<MermaidNamedLinkTypeContainer>) = action.execute(linkTypes)

  override val projectTypes = MermaidNamedProjectTypeContainerImpl(objects)
  override fun projectTypes(action: Action<MermaidNamedProjectTypeContainer>) = action.execute(projectTypes)
}
