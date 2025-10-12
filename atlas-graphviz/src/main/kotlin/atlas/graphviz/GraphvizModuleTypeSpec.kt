/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.graphviz

import atlas.core.ModuleTypeSpec

/**
 * See https://graphviz.org/docs/nodes/ and [NodeAttributes] for custom attributes, which you can set using various
 * Kotlin vars.
 */
public interface GraphvizModuleTypeSpec : ModuleTypeSpec, NodeAttributes
