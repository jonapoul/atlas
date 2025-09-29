/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.mermaid

import modular.core.ModularDsl
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property

interface MermaidLayoutSpec {
  val name: Property<String>
  val properties: MapProperty<String, String>

  @ModularDsl fun put(key: String, value: Any) = properties.put(key, value.toString())
}
