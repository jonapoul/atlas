/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

interface Scenario {
  val rootBuildFile: String
  val submoduleBuildFiles: Map<String, String> get() = emptyMap()
  val gradlePropertiesFile: String get() = ""
}
