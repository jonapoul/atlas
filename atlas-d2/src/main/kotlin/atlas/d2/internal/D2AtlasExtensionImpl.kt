/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.d2.internal

import atlas.core.internal.AtlasExtensionImpl
import atlas.d2.D2AtlasExtension
import atlas.d2.D2NamedLinkTypeContainer
import atlas.d2.D2NamedModuleTypeContainer
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

  override val moduleTypes = D2NamedModuleTypeContainerImpl(objects)
  override fun moduleTypes(action: Action<D2NamedModuleTypeContainer>) = action.execute(moduleTypes)
}
