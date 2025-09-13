/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.gradle

import org.gradle.api.provider.Property

interface OutputSpec {
  val chartRootFilename: Property<String>
  val legendRootFilename: Property<String>

  // All chart files will be placed in each submodule's root folder
  @ModularDsl fun saveChartsInSubmoduleDir()

  // All chart files will be placed in the specified relative path to each submodule's root folder
  @ModularDsl fun saveChartsRelativeToSubmodule(relativeToSubmodule: String?)

  // All legend files will be placed in the root project's root folder
  @ModularDsl fun saveLegendsInRootDir()

  // All legend files will be placed in the specified path relative to the root module's root folder
  @ModularDsl fun saveLegendsRelativeToRootModule(relativeToRoot: String?)
}
