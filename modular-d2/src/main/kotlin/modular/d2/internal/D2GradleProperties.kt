/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.internal

import modular.core.internal.IGradleProperties
import modular.core.internal.bool
import modular.core.internal.enum
import modular.core.internal.int
import modular.core.internal.intEnum
import modular.core.internal.string
import modular.d2.ArrowType
import modular.d2.Direction
import modular.d2.FileFormat
import modular.d2.LayoutEngine
import modular.d2.Theme
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class D2GradleProperties(override val project: Project) : IGradleProperties {
  val arrowType: Provider<ArrowType> = enum("modular.d2.arrowType", default = null)
  val center: Provider<Boolean> = bool("modular.d2.center", default = null)
  val containerLabelPosition: Provider<String> = string("modular.d2.containerLabelPosition", default = null)
  val darkTheme: Provider<Theme> = intEnum("modular.d2.darkTheme", default = null)
  val direction: Provider<Direction> = enum("modular.d2.direction", default = null)
  val fileFormat: Provider<FileFormat> = enum("modular.d2.fileFormat", default = FileFormat.Svg)
  val layoutEngine: Provider<LayoutEngine> = enum("modular.d2.layoutEngine", default = null)
  val pad: Provider<Int> = int("modular.d2.pad", default = null)
  val pathToD2Command: Provider<String> = string("modular.d2.pathToD2Command", default = null)
  val sketch: Provider<Boolean> = bool("modular.d2.sketch", default = null)
  val suppressPlaywrightWarning: Provider<Boolean> = bool("modular.d2.suppressPlaywrightWarning", default = false)
  val theme: Provider<Theme> = intEnum("modular.d2.theme", default = null)
}
