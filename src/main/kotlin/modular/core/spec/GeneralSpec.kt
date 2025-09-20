/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.spec

import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

interface GeneralSpec {
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
   * any module paths or names in configured [ModuleType]s contain a semi-colon.
   * Defaults to ";".
   */
  val separator: Property<String>

  /**
   * Set to true if you want module charts to also show modules that depend on the one in question. This will traverse
   * all the way upwards and downwards. Defaults to false.
   */
  val alsoTraverseUpwards: Property<Boolean>
}
