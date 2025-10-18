/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core

import org.gradle.api.provider.Property

/**
 * Base interface for a framework-specific configuration.
 */
public interface AtlasSpec {
  public val name: String
  public val fileExtension: Property<String>
}
