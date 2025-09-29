/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core

import modular.core.ModularDsl
import modular.core.spec.ModulePathTransformSpec
import modular.core.spec.NamedLinkTypeContainer
import modular.core.spec.NamedModuleTypeContainer
import modular.core.spec.OutputSpec
import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

/**
 * Main entry point for configuring the plugin from your Gradle script.
 */
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
   * A separator character used internally when caching module types and links. You should only need to change this if
   * any module paths or names in configured [modular.core.spec.ModuleTypeSpec]s contain a semi-colon.
   * Defaults to ";".
   */
  val separator: Property<String>

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

  val modulePathTransforms: ModulePathTransformSpec
  @ModularDsl fun modulePathTransforms(action: Action<ModulePathTransformSpec>)

  val moduleTypes: NamedModuleTypeContainer
  @ModularDsl fun moduleTypes(action: Action<NamedModuleTypeContainer>)

  val linkTypes: NamedLinkTypeContainer
  @ModularDsl fun linkTypes(action: Action<NamedLinkTypeContainer>)

  val outputs: OutputSpec
  @ModularDsl fun outputs(action: Action<OutputSpec>)
}
