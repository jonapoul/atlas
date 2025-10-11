/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

@KSerializable
public class MermaidConfig(
  public val layout: String? = null,
  public val layoutProperties: Map<String, String>? = null,
  public val themeVariables: Map<String, String>? = null,
  public val look: Look? = null,
  public val theme: Theme? = null,
  public val animateLinks: Boolean? = null,
) : JSerializable

internal fun MermaidConfig(spec: MermaidSpec): MermaidConfig = MermaidConfig(
  layout = spec.layout.name.orNull,
  layoutProperties = spec.layout.properties.orNull,
  themeVariables = spec.themeVariables.properties.orNull,
  look = spec.look.orNull,
  theme = spec.theme.orNull,
  animateLinks = spec.animateLinks.orNull,
)
