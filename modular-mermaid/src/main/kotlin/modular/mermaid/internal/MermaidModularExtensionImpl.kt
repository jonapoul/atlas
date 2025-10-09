/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.core.InternalModularApi
import modular.core.internal.ModularExtensionImpl
import modular.mermaid.MermaidModularExtension
import modular.mermaid.MermaidNamedLinkTypeContainer
import modular.mermaid.MermaidSpec
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

@InternalModularApi
open class MermaidModularExtensionImpl @Inject constructor(
  objects: ObjectFactory,
  project: Project,
) : ModularExtensionImpl(objects, project), MermaidModularExtension {
  override val mermaid = MermaidSpecImpl(objects, project)
  override fun mermaid(action: Action<MermaidSpec>): MermaidSpec = mermaid.also { action.execute(it) }

  override val linkTypes = MermaidLinkTypeContainer(objects)
  override fun linkTypes(action: Action<MermaidNamedLinkTypeContainer>) = action.execute(linkTypes)
}
