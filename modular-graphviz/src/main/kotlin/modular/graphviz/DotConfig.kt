/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz

import modular.core.InternalModularApi
import modular.core.ModularExtension
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

@KSerializable
public class DotConfig(
  public val displayLinkLabels: Boolean? = null,
  public val layoutEngine: LayoutEngine? = null,
  public val nodeAttributes: Map<String, String>? = null,
  public val edgeAttributes: Map<String, String>? = null,
  public val graphAttributes: Map<String, String>? = null,
) : JSerializable

@InternalModularApi
public fun DotConfig(
  extension: ModularExtension,
  spec: GraphvizSpec,
): DotConfig = DotConfig(
  displayLinkLabels = extension.displayLinkLabels.orNull,
  layoutEngine = spec.layoutEngine.orNull,
  nodeAttributes = spec.node.properties.orNull,
  edgeAttributes = spec.edge.properties.orNull,
  graphAttributes = spec.graph.properties.orNull,
)
