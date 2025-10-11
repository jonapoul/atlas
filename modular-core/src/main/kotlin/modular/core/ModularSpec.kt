/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.core

import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property

public interface ModularSpec {
  public val name: String
  public val fileExtension: Property<String>
}

@ModularDsl
public interface PropertiesSpec {
  public val properties: MapProperty<String, String>
  public fun put(key: String, value: Any): Unit = properties.put(key, value.toString())
  public fun clear(): Unit = properties.set(emptyMap())
}
