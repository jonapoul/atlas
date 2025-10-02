/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi
import modular.core.ModularExtension
import modular.core.spec.LinkTypeSpec
import modular.core.spec.ModuleTypeSpec
import modular.core.spec.NamedLinkTypeContainer
import modular.core.spec.NamedModuleTypeContainer
import modular.core.spec.PathTransformSpec
import modular.core.spec.Replacement
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import javax.inject.Inject

@InternalModularApi
@Suppress("UnnecessaryAbstractClass")
abstract class ModularExtensionImpl(
  objects: ObjectFactory,
  internal val project: Project,
) : ModularExtension {
  @InternalModularApi
  val properties = GradleProperties(project)

  override val generateOnSync = objects.bool(properties.general.generateOnSync)
  override val groupModules = objects.bool(properties.general.groupModules)
  override val ignoredConfigs = objects.set(convention = setOf("debug", "kover", "ksp", "test"))
  override val ignoredModules = objects.set(convention = emptySet<Regex>())
  override val separator = objects.string(properties.general.separator)
  override val alsoTraverseUpwards = objects.bool(properties.general.alsoTraverseUpwards)
  override val printFilesToConsole = objects.bool(properties.general.printFilesToConsole)
  override val checkOutputs = objects.bool(properties.general.checkOutputs)

  override val pathTransforms = PathTransformSpecImpl(objects)
  override fun pathTransforms(action: Action<PathTransformSpec>) = action.execute(pathTransforms)

  override val moduleTypes = ModuleTypeContainer(objects)
  override fun moduleTypes(action: Action<NamedModuleTypeContainer>) = action.execute(moduleTypes)

  override val linkTypes = LinkTypeContainer(objects)
  override fun linkTypes(action: Action<NamedLinkTypeContainer>) = action.execute(linkTypes)

  @InternalModularApi
  companion object {
    @InternalModularApi
    const val NAME = "modular"
  }
}

@InternalModularApi
class PathTransformSpecImpl(objects: ObjectFactory) : PathTransformSpec {
  override val replacements: SetProperty<Replacement> = objects.setProperty(Replacement::class.java)
  override fun replace(pattern: Regex, replacement: String) = replacements.add(Replacement(pattern, replacement))
  override fun replace(pattern: String, replacement: String) = replace(pattern.toRegex(), replacement)
  override fun remove(pattern: Regex) = replace(pattern, replacement = "")
  override fun remove(pattern: String) = remove(pattern.toRegex())
}

@InternalModularApi
abstract class ModuleTypeSpecImpl @Inject constructor(override val name: String) : ModuleTypeSpec {
  @get:Input abstract override val color: Property<String>
  @get:Input abstract override val pathContains: Property<String>
  @get:Input abstract override val pathMatches: Property<String>
  @get:Input abstract override val hasPluginId: Property<String>

  init {
    color.convention("#FFFFFF")
    pathContains.unsetConvention()
    pathMatches.unsetConvention()
    hasPluginId.unsetConvention()
  }
}

internal abstract class LinkTypeSpecImpl @Inject constructor(override val name: String) : LinkTypeSpec {
  @get:Input abstract override val configuration: Property<String>
  @get:Input abstract override val style: Property<String>
  @get:Input abstract override val color: Property<String>

  init {
    configuration.convention(name)
    style.unsetConvention()
    color.unsetConvention()
  }
}
