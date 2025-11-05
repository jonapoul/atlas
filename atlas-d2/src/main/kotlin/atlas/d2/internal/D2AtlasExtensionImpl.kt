package atlas.d2.internal

import atlas.core.internal.AtlasExtensionImpl
import atlas.d2.D2AtlasExtension
import atlas.d2.D2NamedLinkTypeContainer
import atlas.d2.D2NamedProjectTypeContainer
import atlas.d2.D2Spec
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

internal open class D2AtlasExtensionImpl @Inject constructor(
  objects: ObjectFactory,
  project: Project,
) : AtlasExtensionImpl(objects, project), D2AtlasExtension {
  override val d2 = D2SpecImpl(objects, project)
  override fun d2(action: Action<D2Spec>) = action.execute(d2)

  override val linkTypes = D2NamedLinkTypeContainerImpl(objects)
  override fun linkTypes(action: Action<D2NamedLinkTypeContainer>) = action.execute(linkTypes)

  override val projectTypes = D2NamedProjectTypeContainerImpl(objects)
  override fun projectTypes(action: Action<D2NamedProjectTypeContainer>) = action.execute(projectTypes)
}
