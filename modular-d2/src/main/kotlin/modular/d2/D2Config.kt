/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import java.io.Serializable as JSerializable
import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable

@KSerializable
data class D2Config(
  val arrowType: ArrowType? = null,
  val containerLabelPosition: String? = null,
  val direction: Direction? = null,
  val layoutEngine: LayoutEngine? = null,
  val style: Map<String, String>? = null,
  val theme: Theme? = null,
  val darkTheme: Theme? = null,
) : JSerializable

internal fun D2Config(spec: D2Spec): D2Config = D2Config(
  arrowType = spec.arrowType.orNull,
  containerLabelPosition = spec.containerLabelPosition.orNull,
  direction = spec.direction.orNull,
  layoutEngine = spec.layoutEngine.orNull,
  style = spec.style.properties.orNull,
  theme = spec.theme.orNull,
  darkTheme = spec.themeDark.orNull,
)
