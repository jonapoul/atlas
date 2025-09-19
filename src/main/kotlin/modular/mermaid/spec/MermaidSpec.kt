/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.spec

import modular.gradle.ModularDsl
import modular.spec.Spec
import org.gradle.api.Action
import org.gradle.api.provider.Property

interface MermaidSpec : Spec<MermaidLegendSpec, MermaidChartSpec> {
  override val fileExtension: Property<String>

  override var legend: MermaidLegendSpec?
  @ModularDsl override fun legend()
  @ModularDsl override fun legend(action: Action<MermaidLegendSpec>)

  override val chart: MermaidChartSpec
}
