/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.gradle.ModularDsl
import org.gradle.api.provider.Property

interface DotFileChartSpec {
  val arrowHead: Property<String>
  @ModularDsl fun arrowHead(type: ArrowType)

  val arrowTail: Property<String>
  @ModularDsl fun arrowTail(type: ArrowType)

  val layoutEngine: Property<String>
  @ModularDsl fun layoutEngine(layoutEngine: LayoutEngine)
  @ModularDsl fun layoutEngine(layoutEngine: String)

  val dpi: Property<Int>
  val fontSize: Property<Int>
  val rankDir: Property<RankDir>
  val rankSep: Property<Float>
  val showArrows: Property<Boolean>
}
