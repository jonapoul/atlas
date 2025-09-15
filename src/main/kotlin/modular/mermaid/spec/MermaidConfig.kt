/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.spec

import java.io.Serializable as JSerializable
import org.gradle.internal.impldep.kotlinx.serialization.Serializable as KSerializable

@KSerializable
data class MermaidConfig(
  // TBC
) : JSerializable

internal fun MermaidConfig(spec: MermaidChartSpec): MermaidConfig = MermaidConfig(
  // TBC
)
