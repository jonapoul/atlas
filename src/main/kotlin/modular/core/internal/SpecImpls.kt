/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.spec.LinkTypeSpec
import modular.core.spec.ModulePathTransformSpec
import modular.core.spec.ModuleTypeSpec
import modular.core.spec.NamedLinkTypeContainer
import modular.core.spec.NamedModuleTypeContainer
import modular.core.spec.OutputSpec
import modular.core.spec.Spec
import modular.gradle.ModularExtension
import modular.graphviz.internal.GraphvizSpecImpl
import modular.graphviz.spec.GraphvizSpec
import modular.mermaid.internal.MermaidSpecImpl
import modular.mermaid.spec.MermaidSpec
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import javax.inject.Inject

internal open class ModularExtensionImpl @Inject constructor(
  private val objects: ObjectFactory,
  project: Project,
) : ModularExtension {
  internal val properties = GradleProperties(project)

  override val generateOnSync = objects.bool(properties.general.generateOnSync)
  override val groupModules = objects.bool(properties.general.groupModules)
  override val ignoredConfigs = objects.set(convention = setOf("debug", "kover", "ksp", "test"))
  override val ignoredModules = objects.set(convention = emptySet<Regex>())
  override val separator = objects.string(properties.general.separator)
  override val alsoTraverseUpwards = objects.bool(properties.general.alsoTraverseUpwards)
  override val printFilesToConsole = objects.bool(properties.general.printFilesToConsole)
  override val checkOutputs = objects.bool(properties.general.checkOutputs)

  override val modulePathTransforms = ModulePathTransformSpecImpl(objects)
  override fun modulePathTransforms(action: Action<ModulePathTransformSpec>) = action.execute(modulePathTransforms)

  override val moduleTypes = ModuleTypeContainer(objects)
  override fun moduleTypes(action: Action<NamedModuleTypeContainer>) = action.execute(moduleTypes)

  override val linkTypes = LinkTypeContainer(objects)
  override fun linkTypes(action: Action<NamedLinkTypeContainer>) = action.execute(linkTypes)

  override val outputs = OutputSpecImpl(objects, project)
  override fun outputs(action: Action<OutputSpec>) = action.execute(outputs)

  override val specs: NamedDomainObjectContainer<Spec> = objects.domainObjectContainer(Spec::class.java)
  override fun specs(action: Action<NamedDomainObjectContainer<Spec>>) = action.execute(specs)

  override val graphViz: GraphvizSpec
    get() {
      graphViz()
      return specs.getByName(GraphvizSpecImpl.NAME) as GraphvizSpec
    }

  override fun graphViz() = graphViz {}

  override fun graphViz(action: Action<GraphvizSpec>) {
    val spec = specs.findByName(GraphvizSpecImpl.NAME)
      as? GraphvizSpec
      ?: GraphvizSpecImpl(objects, properties)
    action.execute(spec)
    specs.add(spec)
  }

  override val mermaid: MermaidSpec
    get() {
      mermaid()
      return specs.getByName(MermaidSpecImpl.NAME) as MermaidSpec
    }

  override fun mermaid() = mermaid {}

  override fun mermaid(action: Action<MermaidSpec>) {
    val spec = specs.findByName(MermaidSpecImpl.NAME)
      as? MermaidSpec
      ?: MermaidSpecImpl(objects, properties)
    action.execute(spec)
    specs.add(spec)
  }

  internal companion object {
    internal const val NAME = "modular"
  }
}

internal class ModulePathTransformSpecImpl(objects: ObjectFactory) : ModulePathTransformSpec {
  override val replacements: SetProperty<Replacement> = objects.setProperty(Replacement::class.java)
  override fun replace(pattern: Regex, replacement: String) = replacements.add(Replacement(pattern, replacement))
  override fun replace(pattern: String, replacement: String) = replace(pattern.toRegex(), replacement)
  override fun remove(pattern: Regex) = replace(pattern, replacement = "")
  override fun remove(pattern: String) = remove(pattern.toRegex())
}

internal abstract class ModuleTypeSpecImpl @Inject constructor(override val name: String) : ModuleTypeSpec {
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

internal class OutputSpecImpl(objects: ObjectFactory, project: Project) : OutputSpec {
  private val projectDir = project.layout.projectDirectory

  // chartOutputDirectory is a bit of a hack - when this gets configured it'll store the directory relative to the
  // root project. But when we access it in the leaf tasks, we'll manually rewire it to be relative to the submodule.
  // Just a workaround to avoid the awkwardness of configuring something relative to each submodule's directory,
  // without breaking config cache restrictions. This rewiring doesn't happen for legendOutputDirectory, since that's
  // always relative to root project.
  internal val chartDir = objects.directory(convention = projectDir)
  internal val legendDir = objects.directory(convention = projectDir)

  override val chartRootFilename = objects.string(convention = "chart")
  override val legendRootFilename = objects.string(convention = "legend")

  // All chart files will be placed in each submodule's root folder
  override fun saveChartsInSubmoduleDir() = saveChartsRelativeToSubmodule("")

  // All chart files will be placed in the specified relative path to each submodule's root folder
  override fun saveChartsRelativeToSubmodule(relativeToSubmodule: String) =
    chartDir.set(projectDir.dir(relativeToSubmodule))

  // All legend files will be placed in the root project's root folder
  override fun saveLegendsInRootDir() = saveLegendsRelativeToRootModule("")

  // All legend files will be placed in the specified path relative to the root module's root folder
  override fun saveLegendsRelativeToRootModule(relativeToRoot: String) =
    legendDir.set(projectDir.dir(relativeToRoot))
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
