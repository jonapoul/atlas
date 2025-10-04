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
 *     arrowType = ArrowType.Triangle
 *     direction = Direction.Down
 *   }
 * }
 * ```
 */
@ModularDsl
interface D2Spec : ModularSpec {
  val arrowType: Property<ArrowType>
  val containerLabelPosition: Property<String>
  val direction: Property<Direction>

  val style: D2RootStyleSpec
  fun style(action: Action<D2RootStyleSpec>)
}
