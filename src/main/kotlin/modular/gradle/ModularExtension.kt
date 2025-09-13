/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.gradle

import modular.graphviz.spec.GraphVizSpec
import modular.spec.ExperimentalSpec
import modular.spec.ModulePathTransformSpec
import modular.spec.ModuleType
import modular.spec.OutputSpec
import modular.spec.Spec
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

/**
 * Main entry point for configuring the plugin from your Gradle script.
 */
interface ModularExtension {
  val experimental: ExperimentalSpec
  @ModularDsl fun experimental(action: Action<ExperimentalSpec>)

  val modulePathTransforms: ModulePathTransformSpec
  @ModularDsl fun modulePathTransforms(action: Action<ModulePathTransformSpec>)

  val moduleTypes: NamedDomainObjectContainer<ModuleType>
  @ModularDsl fun moduleTypes(action: Action<NamedDomainObjectContainer<ModuleType>>)

  val outputs: OutputSpec
  @ModularDsl fun outputs(action: Action<OutputSpec>)

  val specs: NamedDomainObjectContainer<Spec<*, *>>
  @ModularDsl fun specs(action: Action<NamedDomainObjectContainer<Spec<*, *>>>)

  @ModularDsl fun graphViz()
  @ModularDsl fun graphViz(action: Action<GraphVizSpec>)

  /**
   * When set to true, syncing the IDE (IntelliJ or Android Studio) will automatically trigger regeneration of your
   * module diagrams. Defaults to false.
   */
  val generateOnSync: Property<Boolean>

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
   * any module paths or names in configured [ModuleType]s contain a semi-colon.
   * Defaults to ";".
   */
  val separator: Property<String>

  /**
   * Set to true if you want module charts to also show modules that depend on the one in question. This will traverse
   * all the way upwards and downwards. Defaults to false.
   */
  val supportUpwardsTraversal: Property<Boolean>
}
