/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import modular.core.InternalModularApi
import java.io.Serializable as JSerializable
import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable

@KSerializable
data class D2Config(
  val containerLabelPosition: String? = null,
  val style: Map<String, String>? = null,
) : JSerializable

@InternalModularApi
fun D2Config(spec: D2Spec): D2Config = D2Config(
  containerLabelPosition = spec.containerLabelPosition.orNull,
  style = spec.style.properties.orNull,
)
