/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.spec

import modular.gradle.ModularDsl
import org.gradle.api.provider.Property

/**
 * Use this to configure where your legend and chart output files will be saved. By default, legends are saved in your
 * root project's directory, and module charts are saved in the directory of each module.
 *
 * You can call [saveLegendsRelativeToRootModule] and [saveChartsRelativeToSubmodule] to customise where exactly these
 * will be placed, or use [saveLegendsInRootDir] and [saveChartsInSubmoduleDir] to more explicitly state the default
 * behaviour.
 */
interface OutputSpec {
  /**
   * Output filename of generated module chart files. Defaults to "modules"
   */
  val chartRootFilename: Property<String>

  /**
   * Output filename of generated legend files. Defaults to "legend"
   */
  val legendRootFilename: Property<String>

  /**
   * All module chart files will be placed in each submodule's root folder.
   */
  @ModularDsl fun saveChartsInSubmoduleDir()

  /**
   * All chart files will be placed in the specified relative path to each submodule's root folder.
   */
  @ModularDsl fun saveChartsRelativeToSubmodule(relativeToSubmodule: String)

  /**
   * All legend files will be placed in the root project's root folder.
   */
  @ModularDsl fun saveLegendsInRootDir()

  /**
   * All legend files will be placed in the specified path relative to the root module's root folder.
   */
  @ModularDsl fun saveLegendsRelativeToRootModule(relativeToRoot: String)
}
