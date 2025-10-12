/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core

import org.gradle.api.provider.Property

public interface AtlasSpec {
  public val name: String
  public val fileExtension: Property<String>
}
