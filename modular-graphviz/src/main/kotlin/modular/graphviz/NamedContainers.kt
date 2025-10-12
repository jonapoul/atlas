/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz

import modular.core.ModularDsl
import modular.core.NamedLinkTypeContainer
import modular.core.NamedModuleTypeContainer

@ModularDsl
public interface GraphvizNamedLinkTypeContainer : NamedLinkTypeContainer<GraphvizLinkTypeSpec>

@ModularDsl
public interface GraphvizNamedModuleTypeContainer : NamedModuleTypeContainer<GraphvizModuleTypeSpec>
