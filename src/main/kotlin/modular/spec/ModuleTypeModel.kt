/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.gradle.Color
import org.gradle.internal.impldep.kotlinx.serialization.Serializable

@Serializable
data class ModuleTypeModel(
  val name: String,
  val color: Color,
) : java.io.Serializable
