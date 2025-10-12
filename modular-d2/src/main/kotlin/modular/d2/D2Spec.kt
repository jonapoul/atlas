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
public interface D2Spec : ModularSpec {
  public val animateLinks: Property<Boolean>
  public val center: Property<Boolean>
  public val direction: Property<Direction>
  public val fileFormat: Property<FileFormat>
  public val groupLabelLocation: Property<Location>
  public val groupLabelPosition: Property<Position>
  public val layoutEngine: Property<LayoutEngine>
  public val pad: Property<Int>
  public val pathToD2Command: Property<String>
  public val sketch: Property<Boolean>
  public val theme: Property<Theme>
  public val themeDark: Property<Theme>

  public val rootStyle: D2RootStyleSpec
  public fun rootStyle(action: Action<D2RootStyleSpec>)

  public val globalProps: D2GlobalPropsSpec
  public fun globalProps(action: Action<D2GlobalPropsSpec>)
}

/**
 * https://d2lang.com/tour/style/#root
 */
@ModularDsl
public interface D2RootStyleSpec : PropertiesSpec {
  public var fill: String?
  public var fillPattern: FillPattern?
  public var stroke: String?
  public var strokeWidth: Int?
  public var strokeDash: Int?
  public var doubleBorder: Boolean?
}

/**
 * Use this for any arbitrary global properties that you want to apply to all charts. These properties will be appended
 * to the bottom of the chart. Chances are, you'll want to use some [globs](https://d2lang.com/tour/globs/) in here,
 * so make sure to read the D2 docs on those.
 */
@ModularDsl
public interface D2GlobalPropsSpec : PropertiesSpec {
  public var arrowType: ArrowType?
  public var fillArrowHeads: Boolean?
  public var font: Font?
  public var fontSize: Int?
}
