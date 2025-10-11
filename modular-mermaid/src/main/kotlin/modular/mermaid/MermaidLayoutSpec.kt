/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.mermaid

import modular.core.ModularDsl
import modular.core.PropertiesSpec
import org.gradle.api.provider.Property

@ModularDsl
public interface MermaidLayoutSpec : PropertiesSpec {
  public val name: Property<String>
}
