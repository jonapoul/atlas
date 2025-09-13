/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import org.gradle.api.provider.Property

interface ModuleType {
  val name: String

  // Optional - defaults to white
  val color: Property<String>

  // Exactly one of these is required
  val pathContains: Property<String>
  val pathMatches: Property<Regex>
  val hasPluginId: Property<String>
}
