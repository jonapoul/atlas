/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.internal

import modular.core.InternalModularApi
import modular.core.internal.string
import modular.d2.D2Spec
import modular.d2.D2StyleSpec
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

@InternalModularApi
class D2SpecImpl(
  objects: ObjectFactory,
  project: Project,
) : D2Spec {
  private val properties = D2GradleProperties(project)

  override val name = "D2"
  override val fileExtension = objects.string(convention = "d2")

  override val containerLabelPosition = objects.string(properties.containerLabelPosition)

  override val style = D2StyleSpecImpl(objects)
  override fun style(action: Action<D2StyleSpec>) = action.execute(style)
}

@InternalModularApi
open class D2StyleSpecImpl(objects: ObjectFactory) : D2StyleSpec {
  override val properties = objects
    .mapProperty(String::class.java, String::class.java)
    .convention(null)
}
