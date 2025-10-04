/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import java.io.Serializable as JSerializable
import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable

@KSerializable
class MermaidConfig(
  val layout: String?,
  val layoutProperties: Map<String, String>?,
  val themeVariables: Map<String, String>?,
  val look: Look?,
  val theme: Theme?,
  val animateLinks: Boolean,
) : JSerializable

internal fun MermaidConfig(spec: MermaidSpec): MermaidConfig = MermaidConfig(
  layout = spec.layout.name.orNull,
  layoutProperties = spec.layout.properties.orNull,
  themeVariables = spec.themeVariables.properties.orNull,
  look = spec.look.orNull,
  theme = spec.theme.orNull,
  animateLinks = spec.animateLinks.get(),
)
