/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

@KSerializable
class MermaidConfig(
  val layout: String? = null,
  val layoutProperties: Map<String, String>? = null,
  val themeVariables: Map<String, String>? = null,
  val look: Look? = null,
  val theme: Theme? = null,
  val animateLinks: Boolean? = null,
) : JSerializable

internal fun MermaidConfig(spec: MermaidSpec): MermaidConfig = MermaidConfig(
  layout = spec.layout.name.orNull,
  layoutProperties = spec.layout.properties.orNull,
  themeVariables = spec.themeVariables.properties.orNull,
  look = spec.look.orNull,
  theme = spec.theme.orNull,
  animateLinks = spec.animateLinks.orNull,
)
