/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.spec

import java.io.Serializable as JSerializable
import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable

@KSerializable
data class MermaidConfig(
  val layout: String?,
  val layoutProperties: Map<String, String>?,
  val look: String?,
  val theme: String?,
) : JSerializable

internal fun MermaidConfig(spec: MermaidChartSpec): MermaidConfig = MermaidConfig(
  layout = spec.layout.name.orNull,
  layoutProperties = spec.layout.properties.orNull,
  look = spec.look.orNull,
  theme = spec.theme.orNull,
)
