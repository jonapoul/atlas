/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.gradle

import modular.internal.ModularProperties
import modular.internal.OrderedNamedContainer
import modular.internal.bool
import modular.internal.set
import modular.internal.string
import modular.spec.DotFileSpec
import modular.spec.ModuleNameSpec
import modular.spec.ModuleType
import modular.spec.Spec
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

  @ModularDsl val moduleTypes: NamedDomainObjectContainer<ModuleType> = OrderedNamedContainer(
    container = objects.domainObjectContainer(ModuleType::class.java) { name ->
      objects.newInstance(ModuleType::class.java, name)
    },
  )

  val generateOnSync: Property<Boolean> = objects.bool(properties.generateOnSync)
  val generateReadme: Property<Boolean> = objects.bool(properties.generateReadme)
  val ignoredConfigs: SetProperty<String> = objects.set(convention = setOf("debug", "kover", "ksp", "test"))
  val ignoredModules: SetProperty<Regex> = objects.set(convention = emptySet())
  val separator: Property<String> = objects.string(properties.separator)
  val supportUpwardsTraversal: Property<Boolean> = objects.bool(properties.supportUpwardsTraversal)

  val outputs = OutputSpec(objects, project)
  @ModularDsl fun outputs(action: Action<OutputSpec>) = action.execute(outputs)

  val specs: NamedDomainObjectContainer<Spec<*, *>> = objects.domainObjectContainer(Spec::class.java)

  @ModularDsl fun dotFile(action: Action<DotFileSpec>? = null) {
    val spec = DotFileSpec(objects, project)
    action?.execute(spec)
    specs.add(spec)
  }

  val moduleNames = ModuleNameSpec(objects)
  @ModularDsl fun moduleNames(action: Action<ModuleNameSpec>) = action.execute(moduleNames)

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
