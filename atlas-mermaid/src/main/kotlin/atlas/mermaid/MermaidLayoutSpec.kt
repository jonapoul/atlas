/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package atlas.mermaid

import atlas.core.AtlasDsl
import atlas.core.PropertiesSpec
import org.gradle.api.provider.Property

@AtlasDsl
public interface MermaidLayoutSpec : PropertiesSpec {
  public val name: Property<String>
}
