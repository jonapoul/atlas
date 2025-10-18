/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.d2.internal

import atlas.core.IntEnum
import atlas.core.internal.IGradleProperties
import atlas.core.internal.bool
import atlas.core.internal.enum
import atlas.core.internal.int
import atlas.core.internal.string
import atlas.d2.Direction
import atlas.d2.FileFormat
import atlas.d2.Location
import atlas.d2.Position
import atlas.d2.Theme
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class D2GradleProperties(override val project: Project) : IGradleProperties {
  // Actual config
  val animateLinks: Provider<Boolean> = bool("atlas.d2.animateLinks", default = null)
  val center: Provider<Boolean> = bool("atlas.d2.center", default = null)
  val darkTheme: Provider<Theme> = intEnum("atlas.d2.darkTheme", default = null)
  val direction: Provider<Direction> = enum("atlas.d2.direction", default = null)
  val fileFormat: Provider<FileFormat> = enum("atlas.d2.fileFormat", default = FileFormat.Svg)
  val groupLabelLocation: Provider<Location> = enum("fontSize.d2.groupLabelLocation", default = null)
  val groupLabelPosition: Provider<Position> = enum("atlas.d2.groupLabelPosition", default = null)
  val pad: Provider<Int> = int("atlas.d2.pad", default = null)
  val pathToD2Command: Provider<String> = string("atlas.d2.pathToD2Command", default = null)
  val sketch: Provider<Boolean> = bool("atlas.d2.sketch", default = null)
  val theme: Provider<Theme> = intEnum("atlas.d2.theme", default = null)

  // Warning suppressions
  val suppressPlaywrightWarning: Provider<Boolean> = bool("atlas.d2.suppressPlaywrightWarning", default = false)
  val suppressLabelLocationWarning: Provider<Boolean> = bool("atlas.d2.suppressLabelLocationWarning", default = false)
  val suppressAnimationWarning: Provider<Boolean> = bool("atlas.d2.suppressAnimationWarning", default = false)
}
