/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.mermaid

import modular.core.ModularDsl
import modular.core.ModularSpec
import org.gradle.api.Action
import org.gradle.api.provider.Property

@ModularDsl
public interface MermaidSpec : ModularSpec {
  /**
   * Set a custom layout engine for the diagram. Unset by default, but mermaid will fall back to "dagre".
   * Use [MermaidLayoutSpec.properties] to set any custom key/value properties.
   * See https://mermaid.js.org/config/layouts.html
   */
  public val layout: MermaidLayoutSpec
  public fun layout(action: Action<MermaidLayoutSpec>)
  public fun elk(action: Action<ElkLayoutSpec>? = null)

  /**
   * Also controlled by the `modular.mermaid.chart.look` Gradle property. Defaults to unset.
   * See https://mermaid.js.org/intro/syntax-reference.html#layout-and-look
   */
  public val look: Property<Look>

  /**
   * Also controlled by the `modular.mermaid.chart.theme` Gradle property. Defaults to unset.
   * See https://mermaid.js.org/config/theming.html
   */
  public val theme: Property<Theme>

  public val themeVariables: MermaidThemeVariablesSpec
  public fun themeVariables(action: Action<MermaidThemeVariablesSpec>)

  /**
   * When set to true, all links between modules will have a pretty animation applied to them.
   *
   * WARNING: this doesn't currently work on GitHub or in IntelliJ's mermaid renderers, but it does work on
   * mermaid.live. IntelliJ breaks rendering completely, while GitHub still renders the diagram but not the animation.
   * Not really recommended for practical use, but it does look cool when it works.
   *
   * Also controlled by the `modular.mermaid.chart.animateLinks` Gradle property. Defaults to false.
   * See https://mermaid.js.org/syntax/flowchart.html#turning-an-animation-on
   */
  public val animateLinks: Property<Boolean>
}
