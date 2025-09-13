/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.spec

import modular.gradle.ModularDsl
import org.gradle.api.Action
import org.gradle.api.provider.Property

interface DotFileSpec : Spec<DotFileLegendSpec, DotFileChartSpec> {
  override val extension: Property<String>
  val pathToDotCommand: Property<String>

  override var legend: DotFileLegendSpec?
  override fun legend(): DotFileLegendSpec
  @ModularDsl override fun legend(action: Action<DotFileLegendSpec>)

  override val chart: DotFileChartSpec

  val fileFormats: DotFileOutputFormatSpec
  @ModularDsl fun fileFormats(action: Action<DotFileOutputFormatSpec>)
}
