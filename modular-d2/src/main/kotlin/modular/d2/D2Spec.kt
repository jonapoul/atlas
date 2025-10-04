/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import modular.core.ModularDsl
import modular.core.spec.ModularSpec
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
 *     ...
 *   }
 * }
 * ```
 */
@ModularDsl
interface D2Spec : ModularSpec {
  val containerLabelPosition: Property<String>

  val direction: Property<String>
  fun direction(value: Direction)

  val style: D2RootStyleSpec
  fun style(action: Action<D2RootStyleSpec>)
}
