/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core

import org.gradle.api.provider.MapProperty

@AtlasDsl
public interface PropertiesSpec {
  public val properties: MapProperty<String, String>
  public fun put(key: String, value: Any): Unit = properties.put(key, value.toString())
  public fun clear(): Unit = properties.set(emptyMap())
}
