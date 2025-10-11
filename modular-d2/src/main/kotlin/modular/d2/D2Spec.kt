/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import modular.core.ModularDsl
import modular.core.ModularSpec
import modular.core.PropertiesSpec
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
 *
 *     rootStyle {
 *       ...
 *     }
 *
 *     globalProps {
 *       ...
 *     }
 *   }
 * }
 * ```
 */
@ModularDsl
interface D2Spec : ModularSpec {
  val animateLinks: Property<Boolean>
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

  val globalProps: D2GlobalPropsSpec
  fun globalProps(action: Action<D2GlobalPropsSpec>)
}

/**
 * https://d2lang.com/tour/style/#root
 */
@ModularDsl
interface D2RootStyleSpec : PropertiesSpec {
  var fill: String?
  var fillPattern: FillPattern?
  var stroke: String?
  var strokeWidth: Int?
  var strokeDash: Int?
  var doubleBorder: Boolean?
}

/**
 * Use this for any arbitrary global properties that you want to apply to all charts. These properties will be appended
 * to the bottom of the chart. Chances are, you'll want to use some [globs](https://d2lang.com/tour/globs/) in here,
 * so make sure to read the D2 docs on those.
 */
@ModularDsl
interface D2GlobalPropsSpec : PropertiesSpec {
  var arrowType: ArrowType?
  var fillArrowHeads: Boolean?
  var font: Font?
  var fontSize: Int?
}
