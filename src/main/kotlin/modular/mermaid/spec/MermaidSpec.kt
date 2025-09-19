/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.spec

import modular.spec.Spec
import org.gradle.api.provider.Property

interface MermaidSpec : Spec<MermaidChartSpec> {
  override val fileExtension: Property<String>
  override val chart: MermaidChartSpec
}
