/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable
import java.io.Serializable as JSerializable

@KSerializable
class Replacement(
  val pattern: Regex,
  val replacement: String,
) : JSerializable
