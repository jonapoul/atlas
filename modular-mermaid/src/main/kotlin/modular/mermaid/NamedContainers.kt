/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import modular.core.ModularDsl
import modular.core.NamedLinkTypeContainer
import modular.core.NamedModuleTypeContainer

@ModularDsl
public interface MermaidNamedLinkTypeContainer : NamedLinkTypeContainer<MermaidLinkTypeSpec>

@ModularDsl
public interface MermaidNamedModuleTypeContainer : NamedModuleTypeContainer<MermaidModuleTypeSpec>
