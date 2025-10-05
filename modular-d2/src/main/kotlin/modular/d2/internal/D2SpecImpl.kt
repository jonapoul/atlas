/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.internal

import modular.core.InternalModularApi
import modular.core.internal.PropertiesSpec
import modular.core.internal.PropertiesSpecImpl
import modular.core.internal.bool
import modular.core.internal.boolDelegate
import modular.core.internal.enum
import modular.core.internal.enumDelegate
import modular.core.internal.int
import modular.core.internal.intDelegate
import modular.core.internal.intEnum
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
  internal val properties = D2GradleProperties(project)

  override val name = "D2"
  override val fileExtension = objects.string(convention = "d2")

  override val animateLinks = objects.bool(properties.animateLinks)
  override val arrowType = objects.enum(properties.arrowType)
  override val center = objects.bool(properties.center)
  override val direction = objects.enum(properties.direction)
  override val fileFormat = objects.enum(properties.fileFormat)
  override val font = objects.enum(properties.font)
  override val fontSize = objects.int(properties.fontSize)
  override val groupLabelLocation = objects.enum(properties.groupLabelLocation)
  override val groupLabelPosition = objects.enum(properties.groupLabelPosition)
  override val layoutEngine = objects.enum(properties.layoutEngine)
  override val pad = objects.int(properties.pad)
  override val pathToD2Command = objects.string(properties.pathToD2Command)
  override val sketch = objects.bool(properties.sketch)
  override val theme = objects.intEnum(properties.theme)
  override val themeDark = objects.intEnum(properties.darkTheme)

  override val rootStyle = D2RootStyleSpecImpl(objects)
  override fun rootStyle(action: Action<D2RootStyleSpec>) = action.execute(rootStyle)
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
