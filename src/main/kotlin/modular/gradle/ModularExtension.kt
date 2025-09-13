/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.gradle

import modular.spec.DotFileSpec
import modular.spec.ExperimentalSpec
import modular.spec.ModuleNameSpec
import modular.spec.ModuleType
import modular.spec.Spec
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

interface ModularExtension {
  val experimental: ExperimentalSpec
  @ModularDsl fun experimental(action: Action<ExperimentalSpec>)

  val moduleNames: ModuleNameSpec
  @ModularDsl fun moduleNames(action: Action<ModuleNameSpec>)

  val moduleTypes: NamedDomainObjectContainer<ModuleType>
  @ModularDsl fun moduleTypes(action: Action<NamedDomainObjectContainer<ModuleType>>)

  val outputs: OutputSpec
  @ModularDsl fun outputs(action: Action<OutputSpec>)

  val specs: NamedDomainObjectContainer<Spec<*, *>>
  @ModularDsl fun specs(action: Action<NamedDomainObjectContainer<Spec<*, *>>>)

  val generateOnSync: Property<Boolean>
  val ignoredConfigs: SetProperty<String>
  val ignoredModules: SetProperty<Regex>
  val separator: Property<String>
  val supportUpwardsTraversal: Property<Boolean>

  @ModularDsl fun dotFile()
  @ModularDsl fun dotFile(action: Action<DotFileSpec>)
}
