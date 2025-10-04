/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.internal

import modular.core.internal.IGradleProperties
import modular.core.internal.bool
import modular.core.internal.enum
import modular.mermaid.Look
import modular.mermaid.Theme
import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal class MermaidGradleProperties(override val project: Project) : IGradleProperties {
  val animateLinks: Provider<Boolean> = bool(key = "modular.mermaid.chart.animateLinks", default = false)
  val look: Provider<Look> = enum(key = "modular.mermaid.chart.look", default = null)
  val theme: Provider<Theme> = enum(key = "modular.mermaid.chart.theme", default = null)
}
