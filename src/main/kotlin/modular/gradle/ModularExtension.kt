/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.gradle

import modular.internal.OrderedNamedContainer
import modular.internal.dotFile
import modular.internal.gradleBoolProperty
import modular.internal.gradleStringProperty
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.domainObjectContainer
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.setProperty
import javax.inject.Inject

open class ModularExtension @Inject constructor(
  objects: ObjectFactory,
  project: Project,
) {
  val moduleTypes: NamedDomainObjectContainer<ModuleType> = OrderedNamedContainer(
    container = objects.domainObjectContainer(ModuleType::class) { name ->
      objects.newInstance(ModuleType::class, name)
    }
  )

  val applyToSubprojects: Property<Boolean> = objects
    .property<Boolean>()
    .convention(project.gradleBoolProperty(key = "modular.applyToSubprojects", default = true))

  val supportUpwardsTraversal: Property<Boolean> = objects
    .property<Boolean>()
    .convention(project.gradleBoolProperty(key = "modular.supportUpwardsTraversal", default = false))

  val generateOnSync: Property<Boolean> = objects
    .property<Boolean>()
    .convention(project.gradleBoolProperty(key = "modular.generateOnSync", default = false))

  val generateReadme: Property<Boolean> = objects
    .property<Boolean>()
    .convention(project.gradleBoolProperty(key = "modular.generateReadme", default = false))

  val ignoredModules: SetProperty<Regex> = objects
    .setProperty<Regex>()
    .convention(emptySet())

  val removeModulePrefix: Property<String> = objects
    .property<String>()
    .unsetConvention()

  /**
   * Only change if any of your [Project] names or any [ModuleType] names contain a comma.
   */
  val separator: Property<String> = objects
    .property<String>()
    .convention(project.gradleStringProperty(key = "modular.separator", default = ","))

  val dotFile = ModularDotFileConfig(objects, project)

  fun dotFile(action: Action<ModularDotFileConfig>) = action.execute(dotFile)
}

class ModularDotFileConfig(objects: ObjectFactory, project: Project) {
  val enabled: Property<Boolean> = objects
    .property<Boolean>()
    .convention(project.gradleBoolProperty(key = "modular.dotfile.enabled", default = false))

  val chartDotFile: RegularFileProperty = objects
    .fileProperty()
    .convention(project.dotFile(name = "modules"))

  val legendDotFile: RegularFileProperty = objects
    .fileProperty()
    .convention(project.rootProject.dotFile(name = "legend"))

}
