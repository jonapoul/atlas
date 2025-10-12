/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import modular.core.ModularDsl
import modular.core.NamedLinkTypeContainer
import modular.core.NamedModuleTypeContainer

@ModularDsl
public interface D2NamedLinkTypeContainer : NamedLinkTypeContainer<D2LinkTypeSpec>

@ModularDsl
public interface D2NamedModuleTypeContainer : NamedModuleTypeContainer<D2ModuleTypeSpec>
