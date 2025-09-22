/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.spec

import java.io.Serializable as JSerializable
import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable

@KSerializable
data class DotConfig(
  val arrowHead: String? = null,
  val arrowTail: String? = null,
  val dir: String? = null,
  val dpi: Int? = null,
  val fontSize: Int? = null,
  val layoutEngine: String? = null,
  val rankDir: String? = null,
  val rankSep: Float? = null,
) : JSerializable

internal fun DotConfig(spec: GraphvizSpec): DotConfig = DotConfig(
  arrowHead = spec.arrowHead.orNull,
  arrowTail = spec.arrowTail.orNull,
  dir = spec.dir.orNull,
  dpi = spec.dpi.orNull,
  fontSize = spec.fontSize.orNull,
  layoutEngine = spec.layoutEngine.orNull,
  rankDir = spec.rankDir.orNull,
  rankSep = spec.rankSep.orNull,
)
