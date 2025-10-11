/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.core

import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property

interface ModularSpec {
  val name: String
  val fileExtension: Property<String>
}

@ModularDsl
interface PropertiesSpec {
  val properties: MapProperty<String, String>
  fun put(key: String, value: Any) = properties.put(key, value.toString())
  fun clear() = properties.set(emptyMap())
}
