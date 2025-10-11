/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz

import modular.core.InternalModularApi
import java.io.Serializable as JSerializable
import kotlinx.serialization.Serializable as KSerializable

@KSerializable
class DotConfig(
  val layoutEngine: LayoutEngine? = null,
  val nodeAttributes: Map<String, String>? = null,
  val edgeAttributes: Map<String, String>? = null,
  val graphAttributes: Map<String, String>? = null,
) : JSerializable

@InternalModularApi
fun DotConfig(spec: GraphvizSpec): DotConfig = DotConfig(
  layoutEngine = spec.layoutEngine.orNull,
  nodeAttributes = spec.node.properties.orNull,
  edgeAttributes = spec.edge.properties.orNull,
  graphAttributes = spec.graph.properties.orNull,
)
