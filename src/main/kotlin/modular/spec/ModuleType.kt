/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import javax.inject.Inject

abstract class ModuleType @Inject constructor(val name: String) {
  // Optional - defaults to white
  @get:Input abstract val color: Property<String>

  // Exactly one of these is required
  @get:Input abstract val pathContains: Property<String>
  @get:Input abstract val pathMatches: Property<Regex>
  @get:Input abstract val hasPluginId: Property<String>

  init {
    color.convention("#FFFFFF")
    pathContains.unsetConvention()
    pathMatches.unsetConvention()
    hasPluginId.unsetConvention()
  }
}
