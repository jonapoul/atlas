/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.mermaid.spec

import modular.gradle.ModularDsl
import org.gradle.api.Action
import org.gradle.api.provider.Property

interface MermaidChartSpec {
  /**
   * Set a custom layout engine for the diagram. Unset by default, but mermaid will fall back to "dagre".
   * Use [MermaidLayoutSpec.properties] to set any custom key/value properties.
   * See https://mermaid.js.org/config/layouts.html
   */
  val layout: MermaidLayoutSpec
  @ModularDsl fun layout(action: Action<MermaidLayoutSpec>)
  @ModularDsl fun elk(action: Action<ElkLayoutSpec>? = null)

  /**
   * See https://mermaid.js.org/intro/syntax-reference.html#layout-and-look
   */
  val look: Property<String>
  @ModularDsl fun look(look: Look) = this.look.set(look.string)

  /**
   * See https://mermaid.js.org/config/theming.html
   */
  val theme: Property<String>
  @ModularDsl fun theme(theme: Theme) = this.theme.set(theme.string)
}
