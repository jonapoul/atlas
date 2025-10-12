/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.d2

import atlas.core.AtlasDsl
import atlas.core.NamedLinkTypeContainer
import atlas.core.NamedModuleTypeContainer

@AtlasDsl
public interface D2NamedLinkTypeContainer : NamedLinkTypeContainer<D2LinkTypeSpec>

@AtlasDsl
public interface D2NamedModuleTypeContainer : NamedModuleTypeContainer<D2ModuleTypeSpec>
