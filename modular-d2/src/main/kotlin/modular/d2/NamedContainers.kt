/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import modular.core.LinkTypeSpec
import modular.core.ModularDsl
import modular.core.NamedLinkTypeContainer
import modular.core.NamedModuleTypeContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.kotlin.dsl.register

@ModularDsl
public interface D2NamedLinkTypeContainer : NamedLinkTypeContainer {
  public operator fun String.invoke(
    style: LinkStyle? = null,
    color: String? = null,
    displayName: String = this,
  ): NamedDomainObjectProvider<LinkTypeSpec> = register(this, style, color, displayName)
}

@ModularDsl
public interface D2NamedModuleTypeContainer : NamedModuleTypeContainer<D2ModuleTypeSpec>
