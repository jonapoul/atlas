/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core

import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

/**
 * Main entry point for configuring the plugin from your Gradle script.
 */
@AtlasDsl
public interface AtlasExtension {
  /**
   * When set to true, syncing the IDE (IntelliJ or Android Studio) will automatically trigger regeneration of your
   * module diagrams. Defaults to false. Be careful enabling this on large projects, sync time might extend quite a bit.
   */
  public val generateOnSync: Property<Boolean>

  /**
   * Set to true if you want module charts to gather together groups of modules into bordered containers. E.g. a graph
   * with ":a", ":b" and ":c" won't be grouped at all because they don't share any path segments, but ":a:b" and "a:c"
   * will be grouped together.
   */
  public val groupModules: Property<Boolean>

  /**
   * Use this to configure Gradle [org.gradle.api.artifacts.Configuration]s to block from consideration when collating
   * module diagrams. Defaults to ["debug", "kover", "ksp", "test"].
   */
  public val ignoredConfigs: SetProperty<String>

  /**
   * Use this to block modules from inclusion in your module charts, based on their path string. E.g. a module at
   * ":path:to:my:module" will be ignored if I add `".*:to:my:.*".toRegex()` to this property.
   */
  public val ignoredModules: SetProperty<Regex>

  /**
   * Set to true if you want module charts to also show modules that depend on the one in question. This will traverse
   * the graph both directions and show all upstream and downstream modules. Defaults to false.
   */
  public val alsoTraverseUpwards: Property<Boolean>

  /**
   * Set to true to print the absolute path of any generated files to the Gradle console output. Defaults to false.
   */
  public val printFilesToConsole: Property<Boolean>

  /**
   * Set to true to attach a diffing task to `gradle check`. It will verify that your generated charts match the
   * current state of the project layout, failing if not with a useful error message. Defaults to true.
   */
  public val checkOutputs: Property<Boolean>

  /**
   * Set to true to attach a string label on each module link, showing which configuration caused the link to be
   * created. Defaults to false. When true, the [LinkTypeSpec.name] property will be shown.
   */
  public val displayLinkLabels: Property<Boolean>

  /**
   * Configures any string transformations to apply to module paths when displaying them in the generated charts.
   */
  public val pathTransforms: PathTransformSpec
  public fun pathTransforms(action: Action<PathTransformSpec>)

  /**
   * Configure the set of [ModuleTypeSpec]s to use when identifying modules in your project.
   */
  public val moduleTypes: NamedModuleTypeContainer<*>

  /**
   * Configure the set of [LinkTypeSpec]s to use when identifying links between your modules.
   */
  public val linkTypes: NamedLinkTypeContainer<*>
}
