/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.gradle

import modular.internal.ModularProperties
import modular.internal.OrderedNamedContainer
import modular.spec.DotFileOutputSpec
import modular.spec.ModuleType
import modular.spec.OutputSpec
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import javax.inject.Inject

open class ModularExtension @Inject constructor(
  private val objects: ObjectFactory,
  private val project: Project,
) {
  private val properties = ModularProperties(project)
  val outputs: NamedDomainObjectContainer<OutputSpec<*>> = objects.domainObjectContainer(OutputSpec::class.java)

  val moduleTypes: NamedDomainObjectContainer<ModuleType> = OrderedNamedContainer(
    container = objects.domainObjectContainer(ModuleType::class.java) { name ->
      objects.newInstance(ModuleType::class.java, name)
    },
  )

  val autoApplyLeaves: Property<Boolean> = objects
    .property(Boolean::class.java)
    .convention(properties.autoApplyLeaves)

  val supportUpwardsTraversal: Property<Boolean> = objects
    .property(Boolean::class.java)
    .convention(properties.supportUpwardsTraversal)

  val generateOnSync: Property<Boolean> = objects
    .property(Boolean::class.java)
    .convention(properties.generateOnSync)

  val generateReadme: Property<Boolean> = objects
    .property(Boolean::class.java)
    .convention(properties.generateReadme)

  val ignoredModules: SetProperty<Regex> = objects
    .setProperty(Regex::class.java)
    .convention(emptySet())

  val removeModulePrefix: Property<String> = objects
    .property(String::class.java)
    .convention(properties.removeModulePrefix)

  val ignoredConfigs: SetProperty<String> = objects
    .setProperty(String::class.java)
    .convention(setOf("debug", "kover", "ksp", "test"))

  /**
   * Only change if any of your [Project] names or any [ModuleType] names contain a comma.
   */
  val separator: Property<String> = objects
    .property(String::class.java)
    .convention(properties.separator)

  @ModularDsl
  fun dotFile(action: Action<DotFileOutputSpec>? = null) {
    val config = DotFileOutputSpec(objects, project)
    action?.execute(config)
    outputs.add(config)
  }

  //  fun mermaid(action: Action<MermaidOutputConfig>? = null) {
  //    val config = outputConfigs[MermaidOutputConfig::class]
  //      as? MermaidOutputConfig
  //      ?: MermaidOutputConfig(objects, project)
  //    action?.execute(config)
  //    outputConfigs[MermaidOutputConfig::class] = config
  //  }

  internal companion object {
    internal const val NAME = "modular"
  }
}
