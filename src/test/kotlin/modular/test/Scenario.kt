/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

interface Scenario {
  val settingsFile: String
  val rootBuildFile: String
  val submoduleBuildFiles: Map<String, String>
}
