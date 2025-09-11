/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.gradle

import modular.internal.ModularProperties
import modular.internal.ModuleTypeContainer
import modular.internal.bool
import modular.internal.set
import modular.internal.string
import modular.spec.DotFileSpec
import modular.spec.ExperimentalSpec
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

open class ModularExtension @Inject constructor(private val objects: ObjectFactory, private val project: Project) {
  private val properties = ModularProperties(project)

  val experimental = ExperimentalSpec(objects, project)
  val moduleNames = ModuleNameSpec(objects)
  val outputs = OutputSpec(objects, project)

  val moduleTypes: NamedDomainObjectContainer<ModuleType> = ModuleTypeContainer(objects)
  val specs: NamedDomainObjectContainer<Spec<*, *>> = objects.domainObjectContainer(Spec::class.java)

  val generateOnSync: Property<Boolean> = objects.bool(properties.generateOnSync)
  val generateReadme: Property<Boolean> = objects.bool(properties.generateReadme)
  val ignoredConfigs: SetProperty<String> = objects.set(convention = setOf("debug", "kover", "ksp", "test"))
  val ignoredModules: SetProperty<Regex> = objects.set(convention = emptySet())
  val separator: Property<String> = objects.string(properties.separator)
  val supportUpwardsTraversal: Property<Boolean> = objects.bool(properties.supportUpwardsTraversal)

  @ModularDsl fun experimental(action: Action<ExperimentalSpec>) = action.execute(experimental)
  @ModularDsl fun moduleTypes(action: Action<NamedDomainObjectContainer<ModuleType>>) = action.execute(moduleTypes)
  @ModularDsl fun outputs(action: Action<OutputSpec>) = action.execute(outputs)

  @ModularDsl fun moduleNames(action: Action<ModuleNameSpec>) = action.execute(moduleNames)
  @ModularDsl fun specs(action: Action<NamedDomainObjectContainer<Spec<*, *>>>) = action.execute(specs)

  @ModularDsl
  fun dotFile(action: Action<DotFileSpec>? = null) {
    val spec = specs.findByName(DotFileSpec.NAME) as? DotFileSpec
      ?: DotFileSpec(objects, project).also { specs.add(it) }
    action?.execute(spec)
  }

  internal companion object {
    internal const val NAME = "modular"
  }
}
