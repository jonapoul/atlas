/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.mermaid

import atlas.core.AtlasExtension
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

/**
 * Used to configure the [atlas.mermaid.tasks.WriteMermaidChart] tasks.
 */
@KSerializable
public class MermaidConfig(
  public val displayLinkLabels: Boolean? = null,
  public val layout: String? = null,
  public val layoutProperties: Map<String, String>? = null,
  public val themeVariables: Map<String, String>? = null,
  public val look: Look? = null,
  public val theme: Theme? = null,
  public val animateLinks: Boolean? = null,
) : JSerializable

internal fun MermaidConfig(
  extension: AtlasExtension,
  spec: MermaidSpec,
): MermaidConfig = MermaidConfig(
  displayLinkLabels = extension.displayLinkLabels.orNull,
  layout = spec.layout.name.orNull,
  layoutProperties = spec.layout.properties.orNull,
  themeVariables = spec.themeVariables.properties.orNull,
  look = spec.look.orNull,
  theme = spec.theme.orNull,
  animateLinks = spec.animateLinks.orNull,
)
