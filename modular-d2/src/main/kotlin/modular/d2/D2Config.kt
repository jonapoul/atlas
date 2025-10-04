/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable
import java.io.Serializable as JSerializable

@KSerializable
data class D2Config(
  val arrowType: ArrowType? = null,
  val containerLabelPosition: String? = null,
  val direction: Direction? = null,
  val style: Map<String, String>? = null,
) : JSerializable

internal fun D2Config(spec: D2Spec): D2Config = D2Config(
  arrowType = spec.arrowType.orNull,
  containerLabelPosition = spec.containerLabelPosition.orNull,
  direction = spec.direction.orNull,
  style = spec.style.properties.orNull,
)
