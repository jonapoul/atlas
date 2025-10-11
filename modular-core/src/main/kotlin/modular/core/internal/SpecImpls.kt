/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.InternalModularApi
import modular.core.LinkTypeSpec
import modular.core.ModularExtension
import modular.core.ModuleTypeSpec
import modular.core.PathTransformSpec
import modular.core.Replacement
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import javax.inject.Inject

@InternalModularApi
public abstract class ModularExtensionImpl(
  objects: ObjectFactory,
  internal val project: Project,
) : ModularExtension {
  private val coreProperties = CoreGradleProperties(project)

  override val generateOnSync: Property<Boolean> = objects.bool(coreProperties.generateOnSync)
  override val groupModules: Property<Boolean> = objects.bool(coreProperties.groupModules)
  override val ignoredConfigs: SetProperty<String> = objects.set(convention = setOf("debug", "kover", "ksp", "test"))
  override val ignoredModules: SetProperty<Regex> = objects.set(convention = emptySet())
  override val alsoTraverseUpwards: Property<Boolean> = objects.bool(coreProperties.alsoTraverseUpwards)
  override val printFilesToConsole: Property<Boolean> = objects.bool(coreProperties.printFilesToConsole)
  override val checkOutputs: Property<Boolean> = objects.bool(coreProperties.checkOutputs)

  override val pathTransforms: PathTransformSpecImpl = PathTransformSpecImpl(objects)
  override fun pathTransforms(action: Action<PathTransformSpec>): Unit = action.execute(pathTransforms)

  abstract override val moduleTypes: ModuleTypeContainer<*>
  abstract override val linkTypes: LinkTypeContainer

  @InternalModularApi
  public companion object {
    @InternalModularApi
    public const val NAME: String = "modular"
  }
}

@InternalModularApi
public class PathTransformSpecImpl(objects: ObjectFactory) : PathTransformSpec {
  override val replacements: SetProperty<Replacement> = objects.setProperty(Replacement::class.java)
  override fun replace(pattern: Regex, replacement: String): Unit = replacements.add(Replacement(pattern, replacement))
  override fun replace(pattern: String, replacement: String): Unit = replace(pattern.toRegex(), replacement)
  override fun remove(pattern: Regex): Unit = replace(pattern, replacement = "")
  override fun remove(pattern: String): Unit = remove(pattern.toRegex())
}

@InternalModularApi
public abstract class ModuleTypeSpecImpl @Inject constructor(override val name: String) : ModuleTypeSpec {
  @get:Input public abstract override val color: Property<String>
  @get:Input public abstract override val pathContains: Property<String>
  @get:Input public abstract override val pathMatches: Property<String>
  @get:Input public abstract override val hasPluginId: Property<String>
  @get:Input public abstract override val properties: MapProperty<String, String>

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
