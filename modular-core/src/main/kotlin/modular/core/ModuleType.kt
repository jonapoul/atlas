/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core

import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

@KSerializable
data class ModuleType(
  val name: String,
  val color: String,
  val properties: Map<String, String> = emptyMap(),
) : JSerializable
