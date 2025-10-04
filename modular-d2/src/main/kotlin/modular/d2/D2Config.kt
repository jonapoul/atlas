/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import java.io.Serializable as JSerializable
import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable

@KSerializable
data class D2Config(
  val containerLabelPosition: String? = null,
  val style: Map<String, String>? = null,
  val direction: String? = null,
) : JSerializable

internal fun D2Config(spec: D2Spec): D2Config = D2Config(
  containerLabelPosition = spec.containerLabelPosition.orNull,
  style = spec.style.properties.orNull,
  direction = spec.direction.orNull,
)
