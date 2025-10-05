/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import modular.core.ModularDsl
import modular.core.ModularSpec
import org.gradle.api.Action
import org.gradle.api.provider.Property

/**
 * Used to configure D2 output from Modular. For barebones output to a `.d2` file, you can just add the
 * `"dev.jonpoulton.modular.d2"` gradle plugin. Or for a more fleshed-out config:
 *
 * ```kotlin
 * modular {
 *   // other Modular config
 *
 *   d2 {
 *     animateLinks = true
 *     arrowType = ArrowType.Triangle
 *     center = true
 *     direction = Direction.Down
 *     fileFormat = FileFormat.Svg
 *     groupLabelLocation = Location.Inside
 *     groupLabelPosition = Position.TopCenter
 *     layoutEngine = LayoutEngine.Dagre
 *     pad = 5
 *     pathToD2Command = "/path/to/d2"
 *     sketch = true
 *     theme = Theme.ColorblindClear
 *     themeDark = Theme.DarkMauve
 *   }
 * }
 * ```
 */
@ModularDsl
interface D2Spec : ModularSpec {
  val animateLinks: Property<Boolean>
  val arrowType: Property<ArrowType>
  val center: Property<Boolean>
  val direction: Property<Direction>
  val fileFormat: Property<FileFormat>
  val groupLabelLocation: Property<Location>
  val groupLabelPosition: Property<Position>
  val layoutEngine: Property<LayoutEngine>
  val pad: Property<Int>
  val pathToD2Command: Property<String>
  val sketch: Property<Boolean>
  val theme: Property<Theme>
  val themeDark: Property<Theme>

  val rootStyle: D2RootStyleSpec
  fun rootStyle(action: Action<D2RootStyleSpec>)
}
