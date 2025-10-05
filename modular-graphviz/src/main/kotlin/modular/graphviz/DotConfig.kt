/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz

import modular.core.InternalModularApi
import java.io.Serializable as JSerializable
import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable

@KSerializable
class DotConfig(
  val arrowHead: ArrowType? = null,
  val arrowTail: ArrowType? = null,
  val backgroundColor: String? = null,
  val dir: Dir? = null,
  val dpi: Int? = null,
  val fontSize: Int? = null,
  val layoutEngine: LayoutEngine? = null,
  val rankDir: RankDir? = null,
  val rankSep: Float? = null,
) : JSerializable

@InternalModularApi
fun DotConfig(spec: GraphvizSpec): DotConfig = DotConfig(
  arrowHead = spec.arrowHead.orNull,
  arrowTail = spec.arrowTail.orNull,
  backgroundColor = spec.backgroundColor.orNull,
  dir = spec.dir.orNull,
  dpi = spec.dpi.orNull,
  fontSize = spec.fontSize.orNull,
  layoutEngine = spec.layoutEngine.orNull,
  rankDir = spec.rankDir.orNull,
  rankSep = spec.rankSep.orNull,
)
