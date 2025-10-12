/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.graphviz

import atlas.core.AtlasDsl
import atlas.core.NamedLinkTypeContainer
import atlas.core.NamedModuleTypeContainer

@AtlasDsl
public interface GraphvizNamedLinkTypeContainer : NamedLinkTypeContainer<GraphvizLinkTypeSpec>

@AtlasDsl
public interface GraphvizNamedModuleTypeContainer : NamedModuleTypeContainer<GraphvizModuleTypeSpec>
