/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.internal

import modular.core.InternalModularApi
import modular.core.internal.ModularExtensionImpl
import modular.d2.D2ModularExtension
import modular.d2.D2NamedLinkTypeContainer
import modular.d2.D2Spec
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

@InternalModularApi
open class D2ModularExtensionImpl @Inject constructor(
  objects: ObjectFactory,
  project: Project,
) : ModularExtensionImpl(objects, project), D2ModularExtension {
  override val d2 = D2SpecImpl(objects, project)
  override fun d2(action: Action<D2Spec>): D2Spec = d2.also { action.execute(it) }

  override val linkTypes = D2LinkTypeContainer(objects)
  override fun linkTypes(action: Action<D2NamedLinkTypeContainer>) = action.execute(linkTypes)
}
