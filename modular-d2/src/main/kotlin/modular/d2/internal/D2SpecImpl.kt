/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.internal

import modular.core.InternalModularApi
import modular.core.internal.PropertiesSpec
import modular.core.internal.PropertiesSpecImpl
import modular.core.internal.boolDelegate
import modular.core.internal.enum
import modular.core.internal.enumDelegate
import modular.core.internal.intDelegate
import modular.core.internal.string
import modular.core.internal.stringDelegate
import modular.d2.D2RootStyleSpec
import modular.d2.D2Spec
import modular.d2.FillPattern
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

  override val arrowType = objects.enum(properties.arrowType)
  override val containerLabelPosition = objects.string(properties.containerLabelPosition)
  override val direction = objects.enum(properties.direction)

  override val style = D2RootStyleSpecImpl(objects)
  override fun style(action: Action<D2RootStyleSpec>) = action.execute(style)
}

@InternalModularApi
open class D2RootStyleSpecImpl(
  objects: ObjectFactory,
) : D2RootStyleSpec, PropertiesSpec by PropertiesSpecImpl(objects) {
  override var fill by stringDelegate("fill")
  override var fillPattern by enumDelegate<FillPattern>("fill-pattern")
  override var stroke by stringDelegate("stroke")
  override var strokeWidth by intDelegate("stroke-width")
  override var strokeDash by intDelegate("stroke-dash")
  override var doubleBorder by boolDelegate("double-border")
}
