/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core

import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

/**
 * Main entry point for configuring the plugin from your Gradle script.
 */
@ModularDsl
interface ModularExtension {
  /**
   * When set to true, syncing the IDE (IntelliJ or Android Studio) will automatically trigger regeneration of your
   * module diagrams. Defaults to false.
   */
  val generateOnSync: Property<Boolean>

  /**
   * Set to true if you want module charts to gather together groups of modules into bordered containers. E.g. a graph
   * with ":a", ":b" and ":c" won't be grouped at all because they don't share any path segments, but ":a:b" and "a:c"
   * will be grouped together.
   */
  val groupModules: Property<Boolean>

  /**
   * Use this to configure Gradle [org.gradle.api.artifacts.Configuration]s to block from consideration when collating
   * module diagrams. Defaults to ["debug", "kover", "ksp", "test"].
   */
  val ignoredConfigs: SetProperty<String>

  /**
   * Use this to block modules from inclusion in your module charts, based on their path string. E.g. a module at
   * ":path:to:my:module" will be ignored if I add `".*:to:my:.*".toRegex()` to this property.
   */
  val ignoredModules: SetProperty<Regex>

  /**
   * Set to true if you want module charts to also show modules that depend on the one in question. This will traverse
   * all the way upwards and downwards. Defaults to false.
   */
  val alsoTraverseUpwards: Property<Boolean>

  /**
   * Set to true to print the absolute path of any generated files to the Gradle console output. Defaults to false.
   */
  val printFilesToConsole: Property<Boolean>

  /**
   * Set to true to attach a diffing task to `gradle check` - which will verify that your generated charts match the
   * current state of the project layout. Defaults to true.
   */
  val checkOutputs: Property<Boolean>

  val pathTransforms: PathTransformSpec
  fun pathTransforms(action: Action<PathTransformSpec>)

  val moduleTypes: NamedModuleTypeContainer
  fun moduleTypes(action: Action<NamedModuleTypeContainer>)

  val linkTypes: NamedLinkTypeContainer
  fun linkTypes(action: Action<NamedLinkTypeContainer>)
}
